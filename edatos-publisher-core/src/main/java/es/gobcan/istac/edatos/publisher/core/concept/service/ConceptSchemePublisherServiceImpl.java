package es.gobcan.istac.edatos.publisher.core.concept.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.ConceptSchemeAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class ConceptSchemePublisherServiceImpl implements ConceptSchemePublisherService {

    private final Logger log = LoggerFactory.getLogger(ConceptSchemePublisherServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Autowired
    ExternalOperationService externalOperationService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public CkanPackage retrieveNewCkanPackage(ConceptSchemeAvro conceptScheme) {
        try {
            ConceptSchemeAvro2CkanPackageMapper conceptSchemeAvro2CkanPackageMapper = new ConceptSchemeAvro2CkanPackageMapper(conceptScheme, metadataConfigurationService, messageSource);

            return conceptSchemeAvro2CkanPackageMapper.toNewCkanPackage(externalCategoryService, externalOperationService);
        } catch (Exception e) {
            log.debug("Error in new concept scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(ConceptSchemeAvro conceptScheme, String id) {
        try {
            ConceptSchemeAvro2CkanPackageMapper conceptSchemeAvro2CkanPackageMapper = new ConceptSchemeAvro2CkanPackageMapper(conceptScheme, metadataConfigurationService, messageSource);

            return conceptSchemeAvro2CkanPackageMapper.toExistingCkanPackage(externalCategoryService, externalOperationService, id);
        } catch (Exception e) {
            log.debug("Error in existing concept scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(ConceptSchemeAvro conceptScheme) {
        try {
            ConceptSchemeAvro2CkanPackageMapper conceptSchemeAvro2CkanPackageMapper = new ConceptSchemeAvro2CkanPackageMapper(conceptScheme, metadataConfigurationService, messageSource);

            return conceptSchemeAvro2CkanPackageMapper.getCkanPackageName();
        } catch (Exception e) {
            log.debug("Error in retrieve concept scheme name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(ConceptSchemeAvro conceptScheme, CkanPackage existingPackage, String packageId) {
        try {
            ConceptSchemeAvro2CkanPackageMapper conceptSchemeAvro2CkanPackageMapper = new ConceptSchemeAvro2CkanPackageMapper(conceptScheme, metadataConfigurationService, messageSource);

            return conceptSchemeAvro2CkanPackageMapper.updateResources(packageId, existingPackage.getResources());
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(ConceptSchemeAvro conceptScheme, CkanPackage existingPackage) {
        try {
            ConceptSchemeAvro2CkanPackageMapper conceptSchemeAvro2CkanPackageMapper = new ConceptSchemeAvro2CkanPackageMapper(conceptScheme, metadataConfigurationService, messageSource);

            return conceptSchemeAvro2CkanPackageMapper.createResources(existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
