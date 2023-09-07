package es.gobcan.istac.edatos.publisher.core.service.impl;

import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.domain.CkanPackageRelationshipEntity;
import es.gobcan.istac.edatos.publisher.core.repository.CkanPackageRelationshipRepository;
import es.gobcan.istac.edatos.publisher.core.service.CkanPackageRelationshipService;

@Service
public class CkanPackageRelationshipServiceImpl implements CkanPackageRelationshipService {

    private final CkanPackageRelationshipRepository ckanPackageRelationshipRepository;

    public CkanPackageRelationshipServiceImpl(CkanPackageRelationshipRepository ckanPackageRelationshipRepository) {
        this.ckanPackageRelationshipRepository = ckanPackageRelationshipRepository;
    }

    @Override
    public void delete(CkanPackageRelationshipEntity ckanPackageRelationshipEntity) {
        ckanPackageRelationshipRepository.delete(ckanPackageRelationshipEntity);
    }

    @Override
    public CkanPackageRelationshipEntity getByUrn(String urn) {
        return ckanPackageRelationshipRepository.findByUrn(urn);
    }

    @Override
    public CkanPackageRelationshipEntity updateOrCreate(CkanPackageRelationshipEntity ckanPackageRelationshipEntity) {
        return ckanPackageRelationshipRepository.save(ckanPackageRelationshipEntity);
    }
}
