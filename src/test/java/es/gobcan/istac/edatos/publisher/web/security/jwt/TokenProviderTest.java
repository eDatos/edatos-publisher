package es.gobcan.istac.edatos.publisher.web.security.jwt;

import static es.gobcan.istac.edatos.publisher.web.util.SecurityCookiesUtil.SERVICE_TICKET_COOKIE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import es.gobcan.istac.edatos.publisher.core.domain.InternalEnabledTokenEntity;
import es.gobcan.istac.edatos.publisher.core.repository.InternalEnabledTokenRepository;
import es.gobcan.istac.edatos.publisher.core.service.impl.InternalEnabledTokenServiceImpl;
import es.gobcan.istac.edatos.publisher.web.config.JHipsterExtraProperties;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(SpringRunner.class)
public class TokenProviderTest {

    private final static String ANONYMOUS = "ROLE_ANONYMOUS";
    private final static String SERVICE_TICKET = "ST-13-bGFVKRi9AwprEZutH8HdRgBvQh0estadisticas";

    private final String secretKey = "e5c9ee274ae87bc031adda32e27fa98b9290da83";
    private final long ONE_MINUTE = 60000;
    private JHipsterProperties jHipsterProperties;
    private JHipsterExtraProperties jHipsterExtraProperties;
    private InternalEnabledTokenRepository internalEnabledTokenRepository;
    private TokenProvider tokenProvider;

    @Before
    public void setup() {
        jHipsterProperties = Mockito.mock(JHipsterProperties.class);
        jHipsterExtraProperties = Mockito.mock(JHipsterExtraProperties.class);
        internalEnabledTokenRepository = Mockito.mock(InternalEnabledTokenRepository.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(SERVICE_TICKET_COOKIE, SERVICE_TICKET);
        tokenProvider = new TokenProvider(jHipsterProperties, jHipsterExtraProperties, new InternalEnabledTokenServiceImpl(internalEnabledTokenRepository), request);
        ReflectionTestUtils.setField(tokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", ONE_MINUTE);
        ReflectionTestUtils.setField(tokenProvider, "tokenRenewPeriodInMilliseconds", 20000);
    }

    @Test
    public void testReturnFalseWhenJWThasInvalidSignature() {
        boolean isTokenValid = (tokenProvider.validateToken(createTokenWithDifferentSignature()) != null);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisMalformed() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        String invalidToken = token.substring(1);
        boolean isTokenValid = (tokenProvider.validateToken(invalidToken) != null);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisExpired() {
        ReflectionTestUtils.setField(tokenProvider, "tokenValidityInMilliseconds", -ONE_MINUTE);

        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);

        boolean isTokenValid = (tokenProvider.validateToken(token) != null);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisUnsupported() {
        String unsupportedToken = createUnsupportedToken();

        boolean isTokenValid = (tokenProvider.validateToken(unsupportedToken) != null);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisInvalid() {
        boolean isTokenValid = (tokenProvider.validateToken("") != null);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisNotInTheDatabase() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);
        Mockito.when(internalEnabledTokenRepository.existsByToken(token)).thenReturn(false);
        boolean isTokenValid = (tokenProvider.validateToken(token) != null);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testJwtIsBeingSavedInTheDatabase() {
        Authentication authentication = createAuthentication();
        String token = tokenProvider.createToken(authentication, false);

        verify(internalEnabledTokenRepository, times(1)).save(new InternalEnabledTokenEntity(token, SERVICE_TICKET, any()));
    }

    private Authentication createAuthentication() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(TokenProviderTest.ANONYMOUS));
        return new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities);
    }

    private String createUnsupportedToken() {
        return Jwts.builder().setPayload("payload").signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    private String createTokenWithDifferentSignature() {
        return Jwts.builder().setSubject("anonymous").signWith(SignatureAlgorithm.HS512, "e5c9ee274ae87bc031adda32e27fa98b9290da90").setExpiration(new Date(new Date().getTime() + ONE_MINUTE))
                .compact();
    }
}
