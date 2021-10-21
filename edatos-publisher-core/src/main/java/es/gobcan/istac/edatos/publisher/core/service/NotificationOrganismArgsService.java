package es.gobcan.istac.edatos.publisher.core.service;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;

public interface NotificationOrganismArgsService {

    String[] argsByOrganism(String organism, ExternalUserEntity externalUserEntity, String lopd);
}
