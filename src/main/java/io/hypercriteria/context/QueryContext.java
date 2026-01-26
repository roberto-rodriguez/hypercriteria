package io.hypercriteria.context;

import io.hypercriteria.util.AliasInfo;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import lombok.Getter;

/**
 *
 * @author rrodriguez
 */
@Getter
public class QueryContext {

    //--------- Initializers ---------
    private final EntityManager entityManager;
    private final Class<?> rootType;
    private final boolean distinct;

    //--------- Calculated during initialization ---------
    // Alias → JavaType
    private final LinkedHashMap<String, Class> aliasTypeMap;

    // Alias → JoinNode
    private final Map<String, JoinNode> aliases = new HashMap<>();

    // Join identity registry
    private Map<JoinKey, JoinNode> joins = new LinkedHashMap<>();

    //--------- Set after we know the return type ---------
    private CriteriaBuilder criteriaBuilder;
//
    private Root<?> root;

    private String rootAlias;

    private JoinNode rootNode;

    public QueryContext(
            EntityManager entityManager,
            Class<?> rootType,
            boolean distinct,
            LinkedHashMap<String, Class> aliasTypeMap
    ) {
        this.entityManager = entityManager;
        this.rootType = rootType;
        this.distinct = distinct;
        this.aliasTypeMap = aliasTypeMap;
    }

    public void complete(CriteriaBuilder criteriaBuilder, Root<?> root, String rootAlias,
            LinkedHashMap<String, AliasInfo> joinInfoMap) {
        this.criteriaBuilder = criteriaBuilder;
        this.root = root;
        this.rootAlias = rootAlias;
        this.rootNode = JoinNode.builder()
                .from(root)
                .alias(rootAlias)
                .build();

        explicitJoinRegistration(joinInfoMap);
    }

    private void explicitJoinRegistration(LinkedHashMap<String, AliasInfo> joinInfoMap) {
        for (Map.Entry<String, AliasInfo> e : joinInfoMap.entrySet()) {
            String joinPath = e.getKey();
            AliasInfo aliasInfo = e.getValue();

            JoinNode joinNode = PathResolver.resolveJoinPath(
                    this,
                    joinPath,
                    aliasInfo.getJoinType()
            );

            // Alias collision check
            if (joinNode.getAlias() != null
                    && !joinNode.getAlias().equals(aliasInfo.getAlias())) {

                throw new IllegalArgumentException(
                        "Join path '" + joinPath
                        + "' already has alias '" + joinNode.getAlias() + "'"
                );
            }

            joinNode.setAlias(aliasInfo.getAlias());
            aliases.put(aliasInfo.getAlias(), joinNode);
        }
    }

}
