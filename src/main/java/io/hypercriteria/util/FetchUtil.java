package io.hypercriteria.util;

import io.hypercriteria.Criteria;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import java.util.LinkedHashMap;
import java.util.Arrays;

public final class FetchUtil {

    private FetchUtil() {
    }
 
    public static void registerAliasedFetch(
            Criteria criteria,
            String fetchPath,
            String alias,
            JoinType joinType
    ) {

        Class<?> javaType
                = TypeUtil.resolveJavaType(fetchPath, criteria);

        criteria.getFetchAliasTypeMap().put(alias, javaType);

        criteria.getFetchInfoMap().put(
                fetchPath,
                new AliasInfo(alias, joinType)
        );
    }

    /* =========================================================
       Query-side fetch application
       ========================================================= */
    public static void applyFetches(
            Criteria criteria,
            Root<?> root
    ) {

        if (criteria.getFetchInfoMap().isEmpty()) {
            return;
        }

        // Guardrail: fetch joins only allowed when selecting root
        criteria.getProjection().ifPresent(p -> {
            throw new IllegalStateException(
                    "Fetch joins are only allowed when selecting the root entity"
            );
        });

        LinkedHashMap<String, Fetch<?, ?>> fetchMap
                = new LinkedHashMap<>();

        criteria.getFetchInfoMap().forEach((fetchPath, info) -> {

            String[] segments = fetchPath.split("\\.");

            Fetch<?, ?> currentFetch = null;

            for (int i = 0; i < segments.length; i++) {

                String partialPath = String.join(".",
                        Arrays.copyOfRange(segments, 0, i + 1)
                );

                if (fetchMap.containsKey(partialPath)) {
                    currentFetch = fetchMap.get(partialPath);
                    continue;
                }

                if (currentFetch == null) {
                    currentFetch
                            = root.fetch(segments[i], info.getJoinType());
                } else {
                    currentFetch
                            = currentFetch.fetch(segments[i], info.getJoinType());
                }

                fetchMap.put(partialPath, currentFetch);
            }
        });
    }
}
