package es.gobcan.istac.edatos.publisher.core.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import es.gobcan.istac.edatos.publisher.core.config.CkanProperties;

public class CkanUtil {

    private static final String URN_VERSION_REGEX = "\\([^)]*\\)$";

    public static String getUrnWithoutVersion(String versionUrn) {
        return versionUrn.replaceFirst(URN_VERSION_REGEX, "");
    }

    public static String buildPackageSearchByUrnUrl(String urlBase, String urn) {
        return urlBase + CkanProperties.PACKAGE_SEARCH_PATH + "?q=urn:\"" + getUrnWithoutVersion(urn) + "\"";
    }

    public static String buildPackageCreateUrl(String urlBase) {
        return urlBase + CkanProperties.PACKAGE_CREATE;
    }

    public static String buildResourceCreateUrl(String urlBase) {
        return urlBase + CkanProperties.RESOURCE_CREATE_PATH;
    }

    public static String buildPackagePatchUrl(String urlBase, String id) {
        return urlBase + CkanProperties.PACKAGE_PATCH_PATH + "?id=" + id;
    }

    public static HttpHeaders getBaseHttpHeader(String apiKey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", apiKey);

        return httpHeaders;
    }
}
