package es.gobcan.istac.edatos.publisher.core.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.gobcan.istac.edatos.publisher.core.domain.DataProtectionPolicyEntity;

public class DataProtectionPolicyRepositoryImpl implements DetachEntityRepositoryCustom<DataProtectionPolicyEntity> {

    @PersistenceContext
    private EntityManager entityManager;

    public void detachEntity(DataProtectionPolicyEntity entity) {
        entityManager.detach(entity);
    }
}
