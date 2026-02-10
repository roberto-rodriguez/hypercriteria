package io.fluentcriteria.context;

import io.fluentcriteria.util.AliasInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    private final List<JoinSpec> explicitJoinSpecs;

    //--------- Calculated during initialization ---------
    // Alias → JavaType
    private final LinkedHashMap<String, Class> aliasTypeMap;

    // Alias → JoinNode
    private final Map<String, JoinNode> aliases = new HashMap<>();

    // Join identity registry
    private final Map<JoinKey, JoinNode> joins = new LinkedHashMap<>();

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
            LinkedHashMap<String, Class> aliasTypeMap,
            List<JoinSpec> explicitJoinSpecs
    ) {
        this.entityManager = entityManager;
        this.rootType = rootType;
        this.distinct = distinct;
        this.aliasTypeMap = aliasTypeMap;
        this.explicitJoinSpecs = explicitJoinSpecs;
    }

    public void complete(
            CriteriaBuilder criteriaBuilder,
            Root<?> root,
            String rootAlias
    ) {
        this.criteriaBuilder = criteriaBuilder;
        this.root = root;
        this.rootAlias = rootAlias;
        this.rootNode = JoinNode.builder()
                .from(root)
                .alias(rootAlias)
                .build();

        explicitJoinRegistration();
    }

    private void explicitJoinRegistration() {
        for (JoinSpec spec : explicitJoinSpecs) {
            JoinNode joinNode = PathResolver.resolveJoinPath(
                    this,
                    spec.getPath(),
                    spec.getJoinType(),
                    spec.isFetch(),
                    true, // declaredExplicitly
                    spec.getAlias() // explicitAlias dimension for JoinKey
            );

            // root spec: alias should bind to root node
            if (spec.getPath().isBlank()) {
                joinNode = rootNode;
            }

            if (spec.getAlias() != null) {
                joinNode.setAlias(spec.getAlias());
                aliases.put(spec.getAlias(), joinNode);
            }
        }
    }

//    private void explicitJoinRegistration(
//            LinkedHashMap<String, AliasInfo> joinInfoMap,
//            boolean processingFetch
//    ) {
//        for (Map.Entry<String, AliasInfo> e : joinInfoMap.entrySet()) {
//            String joinPath = e.getKey();
//            AliasInfo aliasInfo = e.getValue();
//
//            JoinNode joinNode = PathResolver.resolveJoinPath(
//                    this,
//                    joinPath,
//                    aliasInfo.getJoinType(),
//                    processingFetch
//            );
//
//            joinNode.setAlias(aliasInfo.getAlias());
//            aliases.put(aliasInfo.getAlias(), joinNode);
//        }
//    }
    //Joins that were declared explicitly but were never used
    public void initializeUnusedExplicitJoins() {
        System.out.println("initializeUnusedExplicitJoins");
        for (Map.Entry<JoinKey, JoinNode> entry : joins.entrySet()) {
            JoinNode joinNode = entry.getValue();

            System.out.println("initializeUnusedExplicitJoins joinNode = " + joinNode);
            System.out.println("initializeUnusedExplicitJoins joinNode.needsLazyInitialization() = " + joinNode.needsLazyInitialization());

            if (joinNode.needsLazyInitialization()) {
                joinNode.toFrom(this);
            }
        }
    }

}
