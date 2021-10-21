package es.gobcan.istac.edatos.publisher.core.service;

import es.gobcan.istac.edatos.publisher.core.domain.DataProtectionPolicyEntity;

public interface DataProtectionPolicyService {

    DataProtectionPolicyEntity find();
    DataProtectionPolicyEntity update(DataProtectionPolicyEntity value);
}
