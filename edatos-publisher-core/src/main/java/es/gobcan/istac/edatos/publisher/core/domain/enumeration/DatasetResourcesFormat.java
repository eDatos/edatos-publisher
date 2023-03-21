package es.gobcan.istac.edatos.publisher.core.domain.enumeration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;

import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;

public enum DatasetResourcesFormat {
    JSON, XML, JSONSTAT, TSV;

    public static String getFormat(String format) {
        if (format.equals(DatasetResourcesFormat.JSONSTAT.name())) {
            return DatasetResourcesFormat.JSON.name();
        }
        return format;
    }

    public static List<String> getEnumList() {
        return Stream.of(DatasetResourcesFormat.values()).map(DatasetResourcesFormat::name).collect(Collectors.toList());
    }

    public static String getMimeType(String format) {
        if (format.equals(DatasetResourcesFormat.JSONSTAT.name()) || format.equals(DatasetResourcesFormat.JSON.name())) {
            return MediaType.APPLICATION_JSON;
        } else if (format.equals(DatasetResourcesFormat.XML.name())) {
            return MediaType.APPLICATION_XML;
        } else if (format.equals(DatasetResourcesFormat.TSV.name())) {
            return Constants.MIME_TYPE_TSV;
        } else {
            return null;
        }

    }
}
