package es.gobcan.istac.edatos.publisher.mock;

import org.siemac.edatos.common.test.mock.ConfigurationServiceMockImpl;
import org.siemac.edatos.core.common.exception.EDatosException;

import es.gobcan.istac.edatos.publisher.core.config.ExternalUsersConfigurationConstants;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

public class MetadataConfigurationServiceMockImpl extends ConfigurationServiceMockImpl implements MetadataConfigurationService {

    @Override
    public boolean retrieveCaptchaEnable() throws EDatosException {
        return retrievePropertyBoolean(ExternalUsersConfigurationConstants.CAPTCHA_ENABLE);
    }

    @Override
    public String retrieveCaptchaProvider() throws EDatosException {
        return retrieveProperty(ExternalUsersConfigurationConstants.CAPTCHA_PROVIDER);
    }

    @Override
    public String retrieveRecaptchaSiteKey() throws EDatosException {
        return retrieveProperty(ExternalUsersConfigurationConstants.CAPTCHA_RECAPTCHA_SITE_KEY);
    }

    @Override
    public String retrieveRecaptchaSecretKey() throws EDatosException {
        return retrieveProperty(ExternalUsersConfigurationConstants.CAPTCHA_RECAPTCHA_SECRET_KEY);
    }
}
