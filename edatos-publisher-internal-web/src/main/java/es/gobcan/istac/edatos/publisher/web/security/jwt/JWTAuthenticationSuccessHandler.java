package es.gobcan.istac.edatos.publisher.web.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import es.gobcan.istac.edatos.publisher.core.config.Constants;
import es.gobcan.istac.edatos.publisher.core.config.MetadataProperties;
import io.github.jhipster.config.JHipsterProperties;

import static es.gobcan.istac.edatos.publisher.web.util.SecurityCookiesUtil.getCookiePath;

public class JWTAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String TOKEN = "token";
    public static final String AUTHENTICATIONTOKEN = "authentication_token_internal";

    private Environment env;
    private long tokenValidityInSeconds;
    private TokenProvider tokenProvider;
    private MetadataProperties metadataProperties;

    public JWTAuthenticationSuccessHandler(TokenProvider tokenProvider, JHipsterProperties jHipsterProperties, MetadataProperties metadataProperties, Environment env) {
        this.tokenProvider = tokenProvider;
        this.tokenValidityInSeconds = jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.metadataProperties = metadataProperties;
        this.env = env;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean rememberMe = false;
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        Cookie cookie = new Cookie(AUTHENTICATIONTOKEN, jwt);
        cookie.setSecure(env.acceptsProfiles(Constants.SPRING_PROFILE_ENV));
        cookie.setMaxAge((int) tokenValidityInSeconds);
        cookie.setHttpOnly(false);
        cookie.setPath(getCookiePath(metadataProperties));
        response.addCookie(cookie);

        // For evict JSESSIONID, invalidate the session of CASFilter
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        handle(request, response, authentication);
    }
}
