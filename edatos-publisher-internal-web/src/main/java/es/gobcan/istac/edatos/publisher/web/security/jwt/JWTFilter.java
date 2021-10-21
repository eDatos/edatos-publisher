package es.gobcan.istac.edatos.publisher.web.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * Filters incoming requests and installs a Spring Security principal if a
 * header corresponding to a valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);

        if (StringUtils.hasText(jwt)) {
            Jws<Claims> jwtClaims = this.tokenProvider.validateToken(jwt);
            if (jwtClaims != null) {
                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                renewTicketIfNecessary(servletResponse, jwtClaims, authentication);
            }

        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Expand the ticket expiration time if necessary (Renew the ticket)
     * Only if the current ticket has less than 30 minutes left.
     *
     * @param servletResponse
     * @param jwtClaims
     * @param authentication
     */
    private void renewTicketIfNecessary(ServletResponse servletResponse, Jws<Claims> jwtClaims, Authentication authentication) {

        if (tokenProvider.checkRenewTicketIsRequired(jwtClaims)) {
            Boolean rememberMe = false;
            String renewedJwt = tokenProvider.createToken(authentication, rememberMe);
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.addHeader(JWTAuthenticationSuccessHandler.AUTHENTICATIONTOKEN, renewedJwt);
            logger.debug("Renewed token JWT: " + renewedJwt);
        }

    }

    private String resolveToken(HttpServletRequest request) {
        // Header
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        // Cookie
        if (request.getCookies() != null) {
            Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(JWTAuthenticationSuccessHandler.AUTHENTICATIONTOKEN)).findFirst();
            if (tokenCookie.isPresent()) {
                return tokenCookie.get().getValue();
            }
        }

        return null;
    }
}
