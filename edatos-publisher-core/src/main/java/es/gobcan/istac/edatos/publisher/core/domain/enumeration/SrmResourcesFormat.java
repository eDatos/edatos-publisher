package es.gobcan.istac.edatos.publisher.core.domain.enumeration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SrmResourcesFormat {
    JSON, XML, TSV, CSV, XLSX;

    public static List<String> getEnumList() {
        return Stream.of(SrmResourcesFormat.values()).map(SrmResourcesFormat::name).collect(Collectors.toList());
    }
}
