package es.gobcan.istac.edatos.publisher.core.category.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.CategorySchemeAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class CategorySchemePublisherServiceImpl implements CategorySchemePublisherService {

    private final Logger log = LoggerFactory.getLogger(CategorySchemePublisherServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public CkanPackage retrieveNewCkanPackage(CategorySchemeAvro categoryScheme) {
        try {

            CategorySchemeAvro2CkanPackageMapper categorySchemeAvro2CkanPackageMapper = new CategorySchemeAvro2CkanPackageMapper(categoryScheme, metadataConfigurationService, messageSource);

            return categorySchemeAvro2CkanPackageMapper.toNewCkanPackage(externalCategoryService);
        } catch (Exception e) {
            log.debug("Error in new category scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(CategorySchemeAvro categoryScheme, String id) {
        try {
            CategorySchemeAvro2CkanPackageMapper categorySchemeAvro2CkanPackageMapper = new CategorySchemeAvro2CkanPackageMapper(categoryScheme, metadataConfigurationService, messageSource);

            return categorySchemeAvro2CkanPackageMapper.toExistingCkanPackage(externalCategoryService, id);
        } catch (Exception e) {
            log.debug("Error in existing category scheme publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(CategorySchemeAvro categoryScheme) {
        try {
            CategorySchemeAvro2CkanPackageMapper categorySchemeAvro2CkanPackageMapper = new CategorySchemeAvro2CkanPackageMapper(categoryScheme, metadataConfigurationService, messageSource);

            return categorySchemeAvro2CkanPackageMapper.getCkanPackageName();
        } catch (Exception e) {
            log.debug("Error in retrieve category scheme name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(CategorySchemeAvro categoryScheme, CkanPackage existingPackage, String packageId) {
        try {
            CategorySchemeAvro2CkanPackageMapper categorySchemeAvro2CkanPackageMapper = new CategorySchemeAvro2CkanPackageMapper(categoryScheme, metadataConfigurationService, messageSource);

            return categorySchemeAvro2CkanPackageMapper.updateResources(packageId, existingPackage.getResources());
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(CategorySchemeAvro categoryScheme, CkanPackage existingPackage) {
        try {
            CategorySchemeAvro2CkanPackageMapper categorySchemeAvro2CkanPackageMapper = new CategorySchemeAvro2CkanPackageMapper(categoryScheme, metadataConfigurationService, messageSource);

            return categorySchemeAvro2CkanPackageMapper.createResources(existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
