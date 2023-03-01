package es.gobcan.istac.edatos.publisher.core.domain.enumeration;

import javax.ws.rs.core.MediaType;

public enum DatasetResourcesFormat {
    JSON, XML, JSONSTAT, TSV, XLS;

    public static String getFormat(String format) {
        if (format.equals(DatasetResourcesFormat.JSONSTAT.name())) {
            return DatasetResourcesFormat.JSON.name();
        }
        return format;
    }

    public static String getMimeType(String format) {
        if (format.equals(DatasetResourcesFormat.JSONSTAT.name()) || format.equals(DatasetResourcesFormat.JSON.name())) {
            return MediaType.APPLICATION_JSON;
        } else if (format.equals(DatasetResourcesFormat.XML.name())) {
            return MediaType.APPLICATION_XML;
        } else if (format.equals(DatasetResourcesFormat.TSV.name())) {
            return MediaType.TEXT_PLAIN;
        } else {
            return null;
        }

    }
}
