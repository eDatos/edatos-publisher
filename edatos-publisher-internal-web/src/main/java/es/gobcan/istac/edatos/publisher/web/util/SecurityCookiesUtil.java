package es.gobcan.istac.edatos.publisher.web.util;

import es.gobcan.istac.edatos.publisher.core.config.MetadataProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import java.util.Arrays;

public class SecurityCookiesUtil {

    public static final String SERVICE_TICKET_COOKIE = "service_ticket";
    public static final String ROOT_PATH = "/";

    public static String getCookiePath(MetadataProperties metadataProperties) {
        if (StringUtils.isBlank(metadataProperties.getInternalAppUrl())) {
            return ROOT_PATH;
        }

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(metadataProperties.getInternalAppUrl()).build();
        String path = uriComponents.getPath();

        if (StringUtils.isBlank(path)) {
            return ROOT_PATH;
        }

        return StringUtils.prependIfMissing(path, ROOT_PATH);
    }

    public static String getServiceTicketCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
            .filter(c -> SERVICE_TICKET_COOKIE.equals(c.getName()))
            .map(Cookie::getValue)
            .findAny().orElse(null);
    }
}
