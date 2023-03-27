package es.gobcan.istac.edatos.publisher.core.domain.enumeration;

import javax.ws.rs.core.MediaType;

import org.siemac.edatos.core.common.exception.EDatosException;

import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;

public enum ResourcesFormat {
    JSON, XML, JSONSTAT, TSV, CSV;

    public static String getFormat(String format) {
        if (format.equals(ResourcesFormat.JSONSTAT.name())) {
            return ResourcesFormat.JSON.name();
        }
        return format;
    }

    public static String getMimeType(String format) {
        if (format.equals(ResourcesFormat.JSONSTAT.name()) || format.equals(ResourcesFormat.JSON.name())) {
            return MediaType.APPLICATION_JSON;
        } else if (format.equals(ResourcesFormat.XML.name())) {
            return MediaType.APPLICATION_XML;
        } else if (format.equals(ResourcesFormat.TSV.name())) {
            return Constants.MIME_TYPE_TSV;
        } else if (format.equals(ResourcesFormat.CSV.name())) {
            return Constants.MIME_TYPE_CSV;
        } else {
            throw new EDatosException(ServiceExceptionType.UNRECOGNIZED_MIME_TYPE, format);
        }
    }
}
