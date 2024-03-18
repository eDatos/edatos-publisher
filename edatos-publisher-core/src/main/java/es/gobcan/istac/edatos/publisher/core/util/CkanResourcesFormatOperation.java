package es.gobcan.istac.edatos.publisher.core.util;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.siemac.edatos.core.common.enume.utils.ResourcesFormat;
import org.siemac.edatos.core.common.enume.utils.ResourcesFormatOperation;
import org.siemac.edatos.core.common.exception.EDatosException;

import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;

public class CkanResourcesFormatOperation extends ResourcesFormatOperation {

    private static final String[] srmResourcesFormat     = {ResourcesFormat.JSON.name(), ResourcesFormat.XML.name(), ResourcesFormat.TSV.name(), ResourcesFormat.CSV.name(),
            ResourcesFormat.XLSX.name()};
    private static final String[] datasetResourcesFormat = {ResourcesFormat.JSON.name(), ResourcesFormat.XML.name(), ResourcesFormat.JSONSTAT.name(), ResourcesFormat.TSV.name(),
            ResourcesFormat.CSV.name(), ResourcesFormat.XLSX.name()};

    @Override
    public String getFormat(String format) {
        if (format.equals(ResourcesFormat.JSONSTAT.name())) {
            return ResourcesFormat.JSON.name();
        }
        return format;
    }

    public String getMimeType(String format, boolean allowAnyMimeType) {
        String mimeType = null;
        if (format.equals(ResourcesFormat.JSONSTAT.name())) {
            mimeType = MediaType.APPLICATION_JSON;
        } else {
            mimeType = super.getMimeType(format);
        }

        if (mimeType == null && !allowAnyMimeType) {
            throw new EDatosException(ServiceExceptionType.UNRECOGNIZED_MIME_TYPE, format);
        }

        return mimeType;
    }

    public static List<String> getSrmResourcesFormat() {
        return Arrays.asList(srmResourcesFormat);
    }

    public static List<String> getDatasetResourcesFormat() {
        return Arrays.asList(datasetResourcesFormat);
    }
}
