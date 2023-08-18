package es.gobcan.istac.edatos.publisher.core.errors;

import org.siemac.edatos.core.common.exception.CommonServiceExceptionType;

public class ServiceExceptionType extends CommonServiceExceptionType {

    /********
     * QUERY
     ********/
    public static final CommonServiceExceptionType QUERY_NOT_SUPPORTED = create("exception.publisher.query.not_supported");

    public static final CommonServiceExceptionType UNRECOGNIZED_MIME_TYPE = create("exception.publisher.unrecognized.mime.type");
}
