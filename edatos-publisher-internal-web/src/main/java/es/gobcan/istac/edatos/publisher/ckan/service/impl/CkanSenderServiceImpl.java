package es.gobcan.istac.edatos.publisher.ckan.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import es.gobcan.istac.edatos.publisher.ckan.notification.error.impl.NotificationError;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResourceResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import es.gobcan.istac.edatos.publisher.exception.constant.WebMessageExceptionsConstants;
import es.gobcan.istac.edatos.publisher.service.CkanService;

@Service
public class CkanSenderServiceImpl implements CkanSenderService {

    private final CkanService ckanService;
    private final NotificationService notificationService;
    private final NotificationError notificationError;

    public CkanSenderServiceImpl(CkanService ckanService, NotificationService notificationService, NotificationError notificationError) {
        this.ckanService = ckanService;
        this.notificationService = notificationService;
        this.notificationError = notificationError;
    }

    @Override
    public CkanPackage getCkanPackageByName(String urn, String packageName) {
        CkanSearchResponse existingDataset = ckanService.getDatasetByName(packageName);
        if (existingDataset != null && existingDataset.isSuccess()) {
            if (existingDataset.getResult().getCount() > 0) {
                return existingDataset.getResult().getResults().get(0);
            } else {
                return new CkanPackage();
            }
        } else {
            notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE,
                    existingDataset.getError(), null, urn);
        }
        return null;
    }

    @Override
    public Boolean createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn, String packageId) {
        try {

            CkanPackageResponse packageResponse;

            if (!StringUtils.isBlank(packageId)) {
                packageResponse = ckanService.updateDataset(ckanPackage);
            } else {
                packageResponse = ckanService.createDataset(ckanPackage);
            }
            if (!packageResponse.isSuccess()) {
                notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE,
                        packageResponse.getError(), null, urn);
                return false;
            }
            return true;
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE, e, urn);
        }
        return false;
    }

    @Override
    public Boolean updateResources(List<CkanResource> resources, String urn) {
        String nameResource = null;

        try {

            CkanResourceResponse resourceResponse;

            for (CkanResource resource : resources) {
                nameResource = resource.getName();
                resourceResponse = ckanService.updateResource(resource);

                if (!resourceResponse.isSuccess()) {
                    notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_UPDATE_RESOURCE, WebMessageExceptionsConstants.MESSAGE_FAILED_UPDATE_RESOURCE,
                            resourceResponse.getError(), null, nameResource, urn);
                    return false;
                }
            }
            return true;
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_UPDATE_RESOURCE, WebMessageExceptionsConstants.MESSAGE_FAILED_UPDATE_RESOURCE, e,
                    nameResource != null ? nameResource : "-", urn);
        }
        return false;
    }

    @Override
    public Boolean createResources(List<CkanResource> resources, String urn) {
        String nameResource = null;
        try {

            CkanResourceResponse resourceResponse;

            for (CkanResource resource : resources) {
                nameResource = resource.getName();
                resourceResponse = ckanService.createResource(resource);

                if (!resourceResponse.isSuccess()) {
                    notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_CREATE_RESOURCE, WebMessageExceptionsConstants.MESSAGE_FAILED_CREATE_RESOURCE,
                            resourceResponse.getError(), null, nameResource, urn);
                    return false;
                }
            }
            return true;
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_CREATE_RESOURCE, WebMessageExceptionsConstants.MESSAGE_FAILED_CREATE_RESOURCE, e,
                    nameResource != null ? nameResource : "-", urn);
        }
        return false;
    }
}
