package es.gobcan.istac.edatos.publisher.core.concept.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.ConceptSchemeAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class ConceptSchemeServiceImpl implements ConceptSchemeService {

    private final Logger log = LoggerFactory.getLogger(ConceptSchemeServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Autowired
    ExternalOperationService externalOperationService;

    @Override
    public CkanPackage retrieveNewCkanPackage(ConceptSchemeAvro conceptScheme) {
        try {
            return ConceptSchemeAvro2CkanPackageMapper.toNewCkanPackage(conceptScheme, metadataConfigurationService, externalCategoryService, externalOperationService);
        } catch (Exception e) {
            log.debug("Error in new concept scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(ConceptSchemeAvro conceptScheme, String id) {
        try {
            return ConceptSchemeAvro2CkanPackageMapper.toExistingCkanPackage(conceptScheme, metadataConfigurationService, externalCategoryService, externalOperationService, id);
        } catch (Exception e) {
            log.debug("Error in existing concept scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(ConceptSchemeAvro conceptScheme) {
        try {
            return ConceptSchemeAvro2CkanPackageMapper.getCkanPackageName(conceptScheme);
        } catch (Exception e) {
            log.debug("Error in retrieve concept scheme name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(ConceptSchemeAvro conceptScheme, CkanPackage existingPackage, String packageId) {
        try {
            return ConceptSchemeAvro2CkanPackageMapper.updateResources(conceptScheme, metadataConfigurationService, packageId, existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(ConceptSchemeAvro conceptScheme, String packageId) {
        try {
            return ConceptSchemeAvro2CkanPackageMapper.createResources(conceptScheme, metadataConfigurationService, packageId);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
