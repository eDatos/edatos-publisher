package es.gobcan.istac.edatos.publisher.ckan.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import es.gobcan.istac.edatos.publisher.ckan.notification.error.impl.NotificationError;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGenericResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResourceResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import es.gobcan.istac.edatos.publisher.exception.constant.WebMessageExceptionsConstants;
import es.gobcan.istac.edatos.publisher.service.CkanService;

@Service
public class CkanSenderServiceImpl implements CkanSenderService {

    private final CkanService            ckanService;
    private final NotificationService    notificationService;
    private final NotificationError      notificationError;
    private MetadataConfigurationService metadataConfigurationService;

    public CkanSenderServiceImpl(CkanService ckanService, NotificationService notificationService, NotificationError notificationError, MetadataConfigurationService metadataConfigurationService) {
        this.ckanService = ckanService;
        this.notificationService = notificationService;
        this.notificationError = notificationError;
        this.metadataConfigurationService = metadataConfigurationService;
    }

    @Override
    public CkanPackage getCkanPackageByName(String urn, String packageName) {
        try {
            CkanSearchResponse existingPackaget = ckanService.getDatasetByName(packageName);
            if (existingPackaget != null && existingPackaget.isSuccess()) {
                if (existingPackaget.getResult().getCount() > 0) {
                    return existingPackaget.getResult().getResults().get(0);
                } else {
                    return new CkanPackage();
                }
            } else {
                notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE,
                        existingPackaget != null ? existingPackaget.getError() : null, null, urn);
            }
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE, e, urn);
        }
        return null;
    }

    @Override
    public CkanPackage createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn, String packageId) {
        try {

            CkanPackageResponse packageResponse;

            if (!StringUtils.isBlank(packageId)) {
                packageResponse = ckanService.updatePackage(ckanPackage);
            } else {
                packageResponse = ckanService.createPackage(ckanPackage);

            }
            if (!packageResponse.isSuccess()) {
                notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE,
                        packageResponse.getError(), null, urn);
                return null;
            } else {
                return packageResponse.getResult();

            }
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE, e, urn);
        }
        return null;
    }

    @Override
    public Boolean updateResources(List<CkanResource> resources, String urn) {
        String nameResource = null;

        try {

            if (resources == null) {
                return true;
            }

            CkanResourceResponse resourceResponse;

            for (CkanResource resource : resources) {
                nameResource = getDefaultPackageName(resource.getName());
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
                nameResource = getDefaultPackageName(resource.getName());
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

    private String getDefaultPackageName(Map<String, String> packageName) {
        String languageDefault = metadataConfigurationService.retrieveLanguageDefault();

        return packageName.entrySet().stream().filter(r -> r.getKey().equals(languageDefault)).map(Map.Entry::getKey).findFirst().orElse("-");

    }

    @Override
    public Boolean deleteCkanPackage(CkanPackage ckanPackage, String urn) {
        try {

            CkanPackageResponse packageResponse = ckanService.deletePackage(ckanPackage);

            if (!packageResponse.isSuccess()) {
                notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_DELETE_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE,
                        packageResponse.getError(), null, urn);
                return false;
            }
            return true;
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_DELETE_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_DELETE_PACKAGE, e, urn);
        }
        return false;
    }

    @Override
    public Boolean deleteResourcesById(List<String> resourcesId, String urn) {
        String nameResource = "-";
        try {

            CkanGenericResponse resourceResponse;

            for (String resourceId : resourcesId) {
                nameResource = resourceId;
                resourceResponse = ckanService.deleteResource(resourceId);

                if (!resourceResponse.isSuccess()) {
                    notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_DELETE_RESOURCE, WebMessageExceptionsConstants.MESSAGE_FAILED_DELETE_RESOURCE,
                            resourceResponse.getError(), null, nameResource, urn);
                    return false;
                }
            }
            return true;
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_DELETE_RESOURCE, WebMessageExceptionsConstants.MESSAGE_FAILED_DELETE_RESOURCE, e,
                    nameResource, urn);
        }
        return false;
    }

    @Override
    public CkanPackage getCkanPackageById(String ckanId) {
        try {
            CkanSearchResponse existingPackaget = ckanService.getDatasetById(ckanId);
            if (existingPackaget != null && existingPackaget.isSuccess()) {
                if (existingPackaget.getResult().getCount() > 0) {
                    return existingPackaget.getResult().getResults().get(0);
                } else {
                    return new CkanPackage();
                }
            } else {
                notificationService.createFailedNotification(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE,
                        existingPackaget != null ? existingPackaget.getError() : null, null, ckanId);
            }
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(WebMessageExceptionsConstants.SUBJECT_FAILED_PUBLICATION_PACKAGE, WebMessageExceptionsConstants.MESSAGE_FAILED_PUBLICATION_PACKAGE, e, ckanId);
        }
        return null;
    }
}
