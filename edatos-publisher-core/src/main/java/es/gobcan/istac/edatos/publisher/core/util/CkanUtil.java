package es.gobcan.istac.edatos.publisher.core.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import es.gobcan.istac.edatos.publisher.core.config.CkanProperties;

public class CkanUtil {

    public static String buildPackageSearchByNameUrl(String urlBase, String name) {
        return urlBase + CkanProperties.PACKAGE_SEARCH_PATH + "?q=name:\"" + name + "\"";
    }

    public static String buildPackageCreateUrl(String urlBase) {
        return urlBase + CkanProperties.PACKAGE_CREATE;
    }

    public static String buildResourceCreateUrl(String urlBase) {
        return urlBase + CkanProperties.RESOURCE_CREATE_PATH;
    }

    public static String buildPackagePatchUrl(String urlBase) {
        return urlBase + CkanProperties.PACKAGE_PATCH_PATH;
    }

    public static String buildPackagePatchResourceUrl(String urlBase) {
        return urlBase + CkanProperties.RESOURCE_PATCH_PATH;
    }

    public static HttpHeaders getBaseHttpHeader(String apiKey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", apiKey);

        return httpHeaders;
    }
}
