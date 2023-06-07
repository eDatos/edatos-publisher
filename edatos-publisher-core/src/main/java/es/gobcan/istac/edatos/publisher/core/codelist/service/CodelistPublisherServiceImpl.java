package es.gobcan.istac.edatos.publisher.core.codelist.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.CodelistAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class CodelistPublisherServiceImpl implements CodelistPublisherService {

    private final Logger log = LoggerFactory.getLogger(CodelistPublisherServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public CkanPackage retrieveNewCkanPackage(CodelistAvro codelist) {
        try {
            CodelistAvro2CkanPackageMapper codelistAvro2CkanPackageMapper = new CodelistAvro2CkanPackageMapper(codelist, metadataConfigurationService, messageSource);

            return codelistAvro2CkanPackageMapper.toNewCkanPackage(externalCategoryService);
        } catch (Exception e) {
            log.debug("Error in new codelist  version publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(CodelistAvro codelist, String id) {
        try {
            CodelistAvro2CkanPackageMapper codelistAvro2CkanPackageMapper = new CodelistAvro2CkanPackageMapper(codelist, metadataConfigurationService, messageSource);

            return codelistAvro2CkanPackageMapper.toExistingCkanPackage(externalCategoryService, id);
        } catch (Exception e) {
            log.debug("Error in existing codelist publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(CodelistAvro codelist) {
        try {
            CodelistAvro2CkanPackageMapper codelistAvro2CkanPackageMapper = new CodelistAvro2CkanPackageMapper(codelist, metadataConfigurationService, messageSource);

            return codelistAvro2CkanPackageMapper.getCkanPackageName();
        } catch (Exception e) {
            log.debug("Error in retrieve codelist name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(CodelistAvro codelist, CkanPackage existingPackage, String packageId) {
        try {
            CodelistAvro2CkanPackageMapper codelistAvro2CkanPackageMapper = new CodelistAvro2CkanPackageMapper(codelist, metadataConfigurationService, messageSource);

            return codelistAvro2CkanPackageMapper.updateResources(packageId, existingPackage.getResources());
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(CodelistAvro codelist, CkanPackage existingPackage) {
        try {
            CodelistAvro2CkanPackageMapper codelistAvro2CkanPackageMapper = new CodelistAvro2CkanPackageMapper(codelist, metadataConfigurationService, messageSource);

            return codelistAvro2CkanPackageMapper.createResources(existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
