package es.gobcan.istac.edatos.publisher.core.service.impl;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;
import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalUserRepository;
import es.gobcan.istac.edatos.publisher.core.security.SecurityUtils;
import es.gobcan.istac.edatos.publisher.core.service.LoginService;
import es.gobcan.istac.edatos.publisher.core.service.validator.LoginValidator;
import org.siemac.edatos.core.common.exception.EDatosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Autowired
    private LoginValidator loginValidator;

    @Override
    public ExternalUserEntity authenticate(String login, String password) {
        log.debug("Request to authenticate a user: {}", login);

        loginValidator.validate(login);

        //@formatter:off
        ExternalUserEntity usuarioToAuthenticate = externalUserRepository.findOneByEmailIgnoreCaseAndDeletionDateIsNull(login)
            .filter(usuario -> SecurityUtils.passwordEncoderMatches(password, usuario.getPassword())).orElseThrow( () -> new EDatosException(ServiceExceptionType.LOGIN_FAILED));
        //@formatter:on

        return usuarioToAuthenticate;

    }
}
