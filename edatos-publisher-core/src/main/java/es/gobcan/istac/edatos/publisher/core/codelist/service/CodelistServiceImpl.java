package es.gobcan.istac.edatos.publisher.core.codelist.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.mappers.CodelistAvro2CkanPackageMapper;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;

@Service
public class CodelistServiceImpl implements CodelistService {

    private final Logger log = LoggerFactory.getLogger(CodelistServiceImpl.class);

    @Autowired
    private MetadataConfigurationService metadataConfigurationService;

    @Autowired
    private ExternalCategoryService externalCategoryService;

    @Override
    public CkanPackage retrieveNewCkanPackage(CodelistAvro codelist) {
        try {
            return CodelistAvro2CkanPackageMapper.toNewCkanPackage(codelist, metadataConfigurationService, externalCategoryService);
        } catch (Exception e) {
            log.debug("Error in new codelist  version publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public CkanPackage retrieveUpdateCkanPackage(CodelistAvro codelist, String id) {
        try {
            return CodelistAvro2CkanPackageMapper.toExistingCkanPackage(codelist, metadataConfigurationService, externalCategoryService, id);
        } catch (Exception e) {
            log.debug("Error in existing codelist publication into ckan: {}", e);
        }
        return null;
    }

    @Override
    public String retrieveCkanPackageName(CodelistAvro codelist) {
        try {
            return CodelistAvro2CkanPackageMapper.getCkanPackageName(codelist);
        } catch (Exception e) {
            log.debug("Error in retrieve codelist name: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveUpdatedResources(CodelistAvro codelist, CkanPackage existingPackage, String packageId) {
        try {
            return CodelistAvro2CkanPackageMapper.updateResources(codelist, metadataConfigurationService, packageId, existingPackage);
        } catch (Exception e) {
            log.debug("Error in retrieve updated resources: {}", e);
        }
        return null;
    }

    @Override
    public List<CkanResource> retrieveNewResources(CodelistAvro codelist, String packageId) {
        try {
            return CodelistAvro2CkanPackageMapper.createResources(codelist, metadataConfigurationService, packageId);
        } catch (Exception e) {
            log.debug("Error in retrieve new resources: {}", e);
        }
        return null;
    }
}
