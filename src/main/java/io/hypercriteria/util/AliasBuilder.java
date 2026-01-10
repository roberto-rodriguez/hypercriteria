package io.hypercriteria.util;

/**
 *
 * @author rrodriguez
 */ 
import io.hypercriteria.criterion.predicate.base.Alias;
import java.util.*;
import java.util.stream.Collectors;

public class AliasBuilder {

    public static List<Alias> build(LinkedHashMap<String, AliasJoinType> fieldToAliasJoinTypeMap) {
        List<Alias> aliasList = new ArrayList<>();

        for (Map.Entry<String, AliasJoinType> entry : fieldToAliasJoinTypeMap.entrySet()) {
            String joinEntity = entry.getKey();
            AliasJoinType aliasJoinType = entry.getValue();

            Alias alias = new Alias(joinEntity, aliasJoinType.getAlias(), aliasJoinType.getJoinType());

            aliasList.add(alias);
        }

        return sortAliases(aliasList);
    }

    /**
     * Sort aliases so that for each entry, the "root" (first segment of name)
     * is already known as an alias (or has no dot at all).
     *
     * Example: app -> ok (no parent) app.file -> parent "app" must exist
     * file.account -> parent "file" must exist
     */
    public static List<Alias> sortAliases(List<Alias> aliases) {
        List<Alias> result = new ArrayList<>(aliases.size());
        Set<String> knownAliases = new HashSet<>();
        Set<Alias> processed = new HashSet<>();

        while (result.size() < aliases.size()) {
            int processedThisRound = 0;

            for (Alias a : aliases) {
                if (processed.contains(a)) {
                    continue;
                }

                String parent = getParentAliasName(a.getName());

                // No dot → parent is the Criteria root, so it's always OK
                if (parent == null || knownAliases.contains(parent)) {
                    result.add(a);
                    processed.add(a);
                    knownAliases.add(a.getAlias());
                    processedThisRound++;
                }
            }

            // If we couldn't process anything in this pass, we have a missing or cyclic dependency
            if (processedThisRound == 0) {
                List<String> unresolved = aliases.stream()
                        .filter(a -> !processed.contains(a))
                        .map(a -> a.getName() + " -> " + a.getAlias())
                        .collect(Collectors.toList());

                throw new IllegalStateException(
                        "Cannot resolve alias ordering. Missing parent aliases for: " + unresolved
                );
            }
        }

        return result;
    }

    /**
     * Parent is the first segment before '.' in the name. "app" -> null (no
     * parent) "app.file" -> "app" "app.file.account" -> "app"
     */
    private static String getParentAliasName(String path) {
        int idx = path.indexOf('.');
        if (idx == -1) {
            return null; // no parent, directly from root criteria
        }
        return path.substring(0, idx);
    }
}
