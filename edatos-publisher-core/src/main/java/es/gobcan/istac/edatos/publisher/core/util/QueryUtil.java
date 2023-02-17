package es.gobcan.istac.edatos.publisher.core.util;


import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;
import es.gobcan.istac.edatos.publisher.core.service.criteria.util.ExternalCkanItemCriteriaProcessor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.siemac.edatos.core.common.exception.EDatosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.arte.libs.grammar.antlr.DefaultQueryExprVisitor;
import com.arte.libs.grammar.antlr.QueryExprCompiler;
import com.arte.libs.grammar.domain.QueryRequest;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;

@Component
public class QueryUtil {

    private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
    private static final String INCLUDE_DELETED_HINT = "HINT INCLUDE_DELETED SET 'true'";
    private QueryExprCompiler queryExprCompiler = new QueryExprCompiler();

    public DetachedCriteria queryToUserExternalUserCriteria(Pageable pageable, String query) {
        return queryToCriteria(pageable, query, new ExternalCkanItemCriteriaProcessor());
    }

    private String pageableSortToQueryString(Pageable pageable) {
        if (pageable == null) {
            return StringUtils.EMPTY;
        }

        return sortToQueryString(pageable.getSort());
    }

    private String sortToQueryString(Sort sort) {
        if (sort == null) {
            return StringUtils.EMPTY;
        }

        StringBuilder result = new StringBuilder();
        for (Order sortOrder : sort) {
            if (!"ID".equalsIgnoreCase(sortOrder.getProperty())) {
                if (result.length() == 0) {
                    result.append(" ORDER BY ");
                } else {
                    result.append(", ");
                }
                result.append(sortOrder.getProperty().toUpperCase());
                if (sortOrder.isAscending()) {
                    result.append(" ").append("ASC");
                } else {
                    result.append(" ").append("DESC");
                }
            }
        }
        return result.toString();
    }

    private DetachedCriteria queryToCriteria(Pageable pageable, String query, AbstractCriteriaProcessor processor) {
        try {
            String sortedQuery = pageableSortToQueryString(pageable);
            return queryToCriteria(sortedQuery, query, processor);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().startsWith("Incorrect order property") || ex.getMessage().startsWith("Incorrect query property")) {
                // Catches a query/sort parameter that isn't recognized.
                throw new EDatosException(ServiceExceptionType.QUERY_NOT_SUPPORTED, getQueryParameter(ex));
            }
            throw ex;
        }
    }

    private DetachedCriteria queryToCriteria(Sort sort, String query, AbstractCriteriaProcessor processor) {
        try {
            String sortedQuery = sortToQueryString(sort);
            return queryToCriteria(sortedQuery, query, processor);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().startsWith("Incorrect order property") || ex.getMessage().startsWith("Incorrect query property")) {
                // Catches a query/sort parameter that isn't recognized.
                throw new EDatosException(ServiceExceptionType.QUERY_NOT_SUPPORTED, getQueryParameter(ex));
            }
            throw ex;
        }
    }

    private DetachedCriteria queryToCriteria(String sortedQuery, String query, AbstractCriteriaProcessor processor) {
        if (query == null) {
            query = StringUtils.EMPTY;
        }

        query += sortedQuery;

        QueryRequest queryRequest = null;
        logger.debug("Petición para mapear query: {}", query);
        if (StringUtils.isNotBlank(query)) {
            DefaultQueryExprVisitor visitor = new DefaultQueryExprVisitor();
            queryExprCompiler.parse(query, visitor);
            queryRequest = visitor.getQueryRequest();
        }
        return processor.process(queryRequest);
    }

    private String getQueryParameter(IllegalArgumentException ex) {
        return StringUtils.substringBetween(ex.getMessage(), "Current: ", " Expected");
    }
}
