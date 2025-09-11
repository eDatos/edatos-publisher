package es.gobcan.istac.edatos.publisher.core.util;

import java.util.Arrays;
import java.util.List;

import org.siemac.edatos.core.common.enume.utils.ResourcesFormat;
import org.siemac.edatos.core.common.enume.utils.ResourcesFormatOperation;
import org.siemac.edatos.core.common.exception.EDatosException;
import org.siemac.edatos.core.common.util.FormatConstants;

import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;

public class CkanResourcesFormatOperation extends ResourcesFormatOperation {

    // this formats defined in edatos-core-utils are a subset of srm formats. Srm at least must provide an implementation for this formats for the resources that are linked in ckan.
    private static final String[] srmResourcesFormat         = {ResourcesFormat.JSON.name(), ResourcesFormat.XML.name(), ResourcesFormat.TSV.name(), ResourcesFormat.CSV.name(),
            ResourcesFormat.XLSX.name()};

    // this formats defined in edatos-core-utils are a subset of statistical-resources formats. At least, statistical-resources must provide an implementation for this formats for the resources that
    // are linked in ckan.
    private static final String[] datasetResourcesFormat     = {ResourcesFormat.JSON.name(), ResourcesFormat.XML.name(), ResourcesFormat.JSONSTAT.name(), ResourcesFormat.TSV.name(),
            ResourcesFormat.CSV.name(), ResourcesFormat.XLSX.name()};

    private static final String[] jaxiDatasetResourcesFormat = {ResourcesFormat.PCAXIS.name(), ResourcesFormat.JSON.name(), ResourcesFormat.JSONSTAT.name()};

    @Override
    public String getFormat(String format) {
        if (format.equals(ResourcesFormat.JSONSTAT.name())) {
            return ResourcesFormat.JSON.name();
        }
        return format;
    }

    public String getMimeType(String format, boolean allowAnyMimeType) {
        String mimeType = null;
        // ckan do not admit jsonstat mime. it must be JSON. the rest of formats must use global mime type stablished in edatos-core-utils.
        if (format.equals(ResourcesFormat.JSONSTAT.name())) {
            mimeType = FormatConstants.MIME_TYPE_JSON;
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

    public static List<String> getJaxiDatasetResourcesFormat() {
        return Arrays.asList(jaxiDatasetResourcesFormat);
    }
}
