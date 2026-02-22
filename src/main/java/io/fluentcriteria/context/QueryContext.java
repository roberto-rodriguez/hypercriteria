package io.fluentcriteria.context;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import lombok.Getter;

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

            boolean hasOn = spec.getOnPredicate().isPresent();

            JoinNode joinNode = PathResolver.resolveJoinPath(
                    this,
                    spec.getPath(),
                    spec.getJoinType(),
                    spec.isFetch(),
                    true,
                    spec.getAlias(),
                    hasOn
            );

            if (spec.getPath().isBlank()) {
                joinNode = rootNode;
            }

            // bind alias -> JoinNode
            if (spec.getAlias() != null) {
                joinNode.setAlias(spec.getAlias());
                aliases.put(spec.getAlias(), joinNode);
            }

            // attach ON predicate to the *terminal join node*
            if (!spec.isFetch() && hasOn) {
                joinNode.setOnPredicate(spec.getOnPredicate().get());
            }
        }
    }

    //Joins that were declared explicitly but were never used
    public void initializeUnusedExplicitJoins() {
        for (Map.Entry<JoinKey, JoinNode> entry : joins.entrySet()) {
            JoinNode joinNode = entry.getValue();
            if (joinNode.needsLazyInitialization()) {
                joinNode.toFrom(this);
            }
        }
    }
}
