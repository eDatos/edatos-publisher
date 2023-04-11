package es.gobcan.istac.edatos.publisher.core.domain.enumeration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum DatasetResourcesFormat {
    JSON, XML, JSONSTAT, TSV, CSV;

    public static List<String> getEnumList() {
        return Stream.of(DatasetResourcesFormat.values()).map(DatasetResourcesFormat::name).collect(Collectors.toList());
    }
}
