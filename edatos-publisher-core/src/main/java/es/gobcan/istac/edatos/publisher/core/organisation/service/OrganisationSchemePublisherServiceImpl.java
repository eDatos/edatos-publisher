package es.gobcan.istac.edatos.publisher.core.organisation.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.OrganisationSchemeAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class OrganisationSchemePublisherServiceImpl implements OrganisationSchemePublisherService {

    private final Logger log = LoggerFactory.getLogger(OrganisationSchemePublisherServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Override
    public CkanPackage retrieveNewCkanPackage(OrganisationSchemeAvro organisationScheme) {
        try {
            return OrganisationSchemeAvro2CkanPackageMapper.toNewCkanPackage(organisationScheme, metadataConfigurationService, externalCategoryService);
        } catch (Exception e) {
            log.debug("Error in new organisation scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(OrganisationSchemeAvro organisationScheme, String id) {
        try {
            return OrganisationSchemeAvro2CkanPackageMapper.toExistingCkanPackage(organisationScheme, metadataConfigurationService, externalCategoryService, id);
        } catch (Exception e) {
            log.debug("Error in existing organisation scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(OrganisationSchemeAvro organisationScheme) {
        try {
            return OrganisationSchemeAvro2CkanPackageMapper.getCkanPackageName(organisationScheme);
        } catch (Exception e) {
            log.debug("Error in retrieve organisation scheme name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(OrganisationSchemeAvro organisationScheme, CkanPackage existingPackage, String packageId) {
        try {
            return OrganisationSchemeAvro2CkanPackageMapper.updateResources(organisationScheme, metadataConfigurationService, packageId, existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(OrganisationSchemeAvro organisationScheme, String packageId) {
        try {
            return OrganisationSchemeAvro2CkanPackageMapper.createResources(organisationScheme, metadataConfigurationService, packageId);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
