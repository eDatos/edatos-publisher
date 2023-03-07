package es.gobcan.istac.edatos.publisher.core.domain.enumeration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MediaType;

public enum CodelistResourcesFormat {
    JSON, XML;

    public static String getFormat(String format) {
        return format;
    }

    public static List<String> getEnumList() {
        return Stream.of(CodelistResourcesFormat.values()).map(CodelistResourcesFormat::name).collect(Collectors.toList());
    }

    public static String getMimeType(String format) {
        if (format.equals(CodelistResourcesFormat.JSON.name())) {
            return MediaType.APPLICATION_JSON;
        } else if (format.equals(CodelistResourcesFormat.XML.name())) {
            return MediaType.APPLICATION_XML;
        } else {
            return null;
        }

    }
}
