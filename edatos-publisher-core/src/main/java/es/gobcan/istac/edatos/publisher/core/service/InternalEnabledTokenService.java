package es.gobcan.istac.edatos.publisher.core.service;

import es.gobcan.istac.edatos.publisher.core.domain.InternalEnabledTokenEntity;

public interface InternalEnabledTokenService {

    void deleteByServiceTicket(String serviceTicket);
    boolean existsByToken(String token);
    InternalEnabledTokenEntity updateOrCreate(InternalEnabledTokenEntity enabledTokenEntity);
}
