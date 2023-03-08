package es.gobcan.istac.edatos.publisher.core.category.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.CategorySchemeAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class CategorySchemeServiceImpl implements CategorySchemeService {

    private final Logger log = LoggerFactory.getLogger(CategorySchemeServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Override
    public CkanPackage retrieveNewCkanPackage(CategorySchemeAvro categoryScheme) {
        try {
            return CategorySchemeAvro2CkanPackageMapper.toNewCkanPackage(categoryScheme, metadataConfigurationService, externalCategoryService);
        } catch (Exception e) {
            log.debug("Error in new category scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(CategorySchemeAvro categoryScheme, String id) {
        try {
            return CategorySchemeAvro2CkanPackageMapper.toExistingCkanPackage(categoryScheme, metadataConfigurationService, externalCategoryService, id);
        } catch (Exception e) {
            log.debug("Error in existing category scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(CategorySchemeAvro categoryScheme) {
        try {
            return CategorySchemeAvro2CkanPackageMapper.getCkanPackageName(categoryScheme);
        } catch (Exception e) {
            log.debug("Error in retrieve category scheme name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(CategorySchemeAvro categoryScheme, CkanPackage existingPackage, String packageId) {
        try {
            return CategorySchemeAvro2CkanPackageMapper.updateResources(categoryScheme, metadataConfigurationService, packageId, existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(CategorySchemeAvro categoryScheme, String packageId) {
        try {
            return CategorySchemeAvro2CkanPackageMapper.createResources(categoryScheme, metadataConfigurationService, packageId);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
