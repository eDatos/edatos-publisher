package es.gobcan.istac.edatos.publisher.core.security;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public final class SecurityUtils {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JHI_AUTHENTICATIONTOKEN = "authenticationtoken";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private SecurityUtils() {
    }

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }

    public static String passwordEncoder(String password) {
        return newPasswordEncoder().encode(password);
    }

    public static Boolean passwordEncoderMatches(String password, String userPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, userPassword);
    }

    private static PasswordEncoder newPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String resolveToken(HttpServletRequest request) {
        // Header
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        // Cookie
        if (request.getCookies() != null) {
            Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(JHI_AUTHENTICATIONTOKEN)).findFirst();
            if (tokenCookie.isPresent()) {
                return tokenCookie.get().getValue();
            }
        }

        return null;
    }

}
