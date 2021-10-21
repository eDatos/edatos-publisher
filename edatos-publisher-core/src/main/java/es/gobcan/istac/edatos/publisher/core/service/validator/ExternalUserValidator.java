package es.gobcan.istac.edatos.publisher.core.service.validator;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;
import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalUserRepository;
import org.siemac.edatos.core.common.exception.EDatosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExternalUserValidator extends AbstractValidator<ExternalUserEntity> {

    @Autowired
    ExternalUserRepository externalUserRepository;

    public void checkEmailEnUso(ExternalUserEntity externalUser) {
        final String email = externalUser.getEmail();
        if (externalUserRepository.findOneByEmail(email).isPresent()) {
            final Optional<ExternalUserEntity> existingUserWithMail = Optional.ofNullable(externalUser.getId()).isPresent()
                    ? externalUserRepository.findOneByEmailAndIdNot(email, externalUser.getId())
                    : externalUserRepository.findOneByEmail(email);
            if (existingUserWithMail.isPresent()) {
                throw new EDatosException(ServiceExceptionType.EXTERNAL_USER_CREATE);
            }
        }
    }

    public void ckeckEmailExists(String email) {
        if (!externalUserRepository.findOneByEmailAndDeletionDateIsNull(email).isPresent()) {
            throw new EDatosException(ServiceExceptionType.EXTERNAL_USER_NOT_EXISTS);
        }
    }
}
