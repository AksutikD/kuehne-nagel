package com.logistic.kuehnenagel.util;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.function.Supplier;

/**
 * Utility class for <code>JpaSpecificationExecutor</code> implementations.
 * It provides methods to create 'search request' specifications (in the sense of Domain Driven Design).
 */
public final class SearchUtils {

    public static final int ROOT = 0;

    private SearchUtils() { }

    /**
     * Creates <code>Specification</code> statement.
     *
     * @param parameterGetter a supplier that holds the parameter for SQL search condition
     * @param entityParams    an array of an entity parameters
     * @param <T>             the type of the {@link Root} the resulting {@literal Specification} operates on
     * @param <E>             the type of results supplied by 'parameterGetter'
     *
     * @return a specification or null if a condition parameter wasn't passed
     */
    public static <T, E> Specification<T> createEqualsStatement(
            Supplier<E> parameterGetter,
            String... entityParams) {

        Specification<T> specification = null;

        if (parameterGetter.get() != null) {
            specification = (root, query, builder)
                    -> builder.equal(getExpression(root, entityParams), parameterGetter.get());
        }

        return specification;
    }

    private static <T, E> Expression<E> getExpression(Root<T> queryRoot, String[] params) {
        Path<E> expression = queryRoot.get(params[ROOT]);
        if (params.length > 1) {
            for (int i = 1; i < params.length; i++) {
                expression = expression.get(params[i]);
            }
        }
        return expression;
    }
}