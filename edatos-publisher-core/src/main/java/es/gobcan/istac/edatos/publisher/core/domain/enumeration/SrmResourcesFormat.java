package es.gobcan.istac.edatos.publisher.core.domain.enumeration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;

import org.siemac.edatos.core.common.exception.EDatosException;

import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;

public enum SrmResourcesFormat {
    JSON, XML, TSV, CSV;

    public static String getFormat(String format) {
        return format;
    }

    public static List<String> getEnumList() {
        return Stream.of(SrmResourcesFormat.values()).map(SrmResourcesFormat::name).collect(Collectors.toList());
    }

    public static String getMimeType(String format) {
        if (format.equals(SrmResourcesFormat.JSON.name())) {
            return MediaType.APPLICATION_JSON;
        } else if (format.equals(SrmResourcesFormat.XML.name())) {
            return MediaType.APPLICATION_XML;
        } else if (format.equals(SrmResourcesFormat.TSV.name())) {
            return Constants.MIME_TYPE_TSV;
        } else if (format.equals(SrmResourcesFormat.CSV.name())) {
            return Constants.MIME_TYPE_CSV;
        } else {
            throw new EDatosException(ServiceExceptionType.UNRECOGNIZED_MIME_TYPE, format);
        }
    }
}
