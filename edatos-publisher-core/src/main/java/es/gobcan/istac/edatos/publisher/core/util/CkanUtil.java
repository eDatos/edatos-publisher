package es.gobcan.istac.edatos.publisher.core.util;

import es.gobcan.istac.edatos.publisher.core.config.CkanProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class CkanUtil {

    private final static String URN_VERSION_REGEX = "\\([^)]*\\)$";

    public static String getUrnWithoutVersion(String versionUrn) {
        return versionUrn.replaceFirst(URN_VERSION_REGEX, "");
    }

    public static String buildPackageSearchByUrnUrl(String urn) {
        return CkanProperties.BASE_URL + CkanProperties.PACKAGE_SEARCH_PATH + "?q=urn:\"" + getUrnWithoutVersion(urn) + "\"";
    }

    public static String buildPackageCreateUrl() {
        return CkanProperties.BASE_URL + CkanProperties.PACKAGE_CREATE;
    }

    public static String buildResourceCreateUrl() {
        return CkanProperties.BASE_URL + CkanProperties.RESOURCE_CREATE_PATH;
    }

    public static String buildPackagePatchUrl(String id) {
        return CkanProperties.BASE_URL + CkanProperties.PACKAGE_PATCH_PATH + "?id=" + id;
    }

    public static HttpHeaders getBaseHttpHeader(RestTemplate restTemplate, String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", CkanProperties.AUTHORIZATION);

        return httpHeaders;
    }
}
