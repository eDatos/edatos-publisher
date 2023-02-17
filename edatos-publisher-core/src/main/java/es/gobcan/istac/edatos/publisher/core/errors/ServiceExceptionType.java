package es.gobcan.istac.edatos.publisher.core.errors;

import org.siemac.edatos.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {

    /********
     * QUERY
     ********/
    public static final CommonServiceExceptionType QUERY_NOT_SUPPORTED = create("exception.external_users.query.not_supported");
}
