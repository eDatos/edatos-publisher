package es.gobcan.istac.edatos.publisher.service.impl;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.domain.CkanPackageRelationshipEntity;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.service.CkanPackageRelationshipService;

public class PublicationConsumerProcess {

    @Autowired
    CkanSenderService              ckanSenderService;
    @Autowired
    CkanPackageRelationshipService ckanPackageRelationshipService;

    public CkanPackage getCkanPackage(String urnWithoutVersion) {

        CkanPackageRelationshipEntity ckanPackageRelationshipEntity = ckanPackageRelationshipService.getByUrn(urnWithoutVersion);

        CkanPackage actualPackage = null;
        if (ckanPackageRelationshipEntity != null && !StringUtils.isEmpty(ckanPackageRelationshipEntity.getCkanId())) {
            actualPackage = ckanSenderService.getCkanPackageById(ckanPackageRelationshipEntity.getCkanId());
            if (actualPackage == null || actualPackage.getId() == null) {
                ckanPackageRelationshipService.delete(ckanPackageRelationshipEntity);
            }
        } else {
            actualPackage = getCkanPackageByName(urnWithoutVersion);
        }

        if (actualPackage == null) {
            actualPackage = new CkanPackage();
        }

        return actualPackage;
    }

    public CkanPackageRelationshipEntity createPackageRelationshipEntity(String urnWithoutVersion, String ckanId) {

        CkanPackageRelationshipEntity newCkanPackageRelationshipEntity = new CkanPackageRelationshipEntity(urnWithoutVersion, ckanId);
        return ckanPackageRelationshipService.updateOrCreate(newCkanPackageRelationshipEntity);
    }

    /*
     * Compatibility with published datasets that are in ckan but the relationship with the ckan id has not been store in publisher. Because of this, it must be searched by name
     */
    private CkanPackage getCkanPackageByName(String urnWithoutVersion) {
        CkanPackage actualPackage = null;
        if (urnWithoutVersion.length() <= Constants.MAX_SIZE_PACKAGE_CKAN_NAME) {
            actualPackage = ckanSenderService.getCkanPackageByName(urnWithoutVersion, urnWithoutVersion);
            if (actualPackage != null && actualPackage.getId() != null) {
                createPackageRelationshipEntity(urnWithoutVersion, actualPackage.getId());
            }
        }
        return actualPackage;
    }

}
