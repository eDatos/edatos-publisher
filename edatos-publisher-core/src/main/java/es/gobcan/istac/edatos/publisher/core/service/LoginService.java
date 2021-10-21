package es.gobcan.istac.edatos.publisher.core.service;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;

public interface LoginService {

    public ExternalUserEntity authenticate(String login, String password);
}
