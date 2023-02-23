package es.gobcan.istac.edatos.publisher.ckan.service.dataset.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import es.gobcan.istac.edatos.publisher.ckan.notification.error.impl.NotificationError;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import es.gobcan.istac.edatos.publisher.service.CkanService;

@Service
public class DatasetSenderServiceImpl implements DatasetSenderService {

    private final CkanService ckanService;
    private final NotificationService notificationService;
    private final NotificationError notificationError;

    public DatasetSenderServiceImpl(CkanService ckanService, NotificationService notificationService, NotificationError notificationError) {
        this.ckanService = ckanService;
        this.notificationService = notificationService;
        this.notificationError = notificationError;
    }

    @Override
    public void createOrUpdateCkanPackage(CkanPackage ckanPackage, String urn) {

        try {
            CkanSearchResponse existingDataset = ckanService.getDatasetByUrn(urn);

            if (existingDataset.isSuccess()) {
                CkanPackageResponse packageResponse;

                // TODO EDATOS-3460 es posible actualizar un dataset? en principio no.
                if (existingDataset.getResult().getCount() > 0) {
                    packageResponse = ckanService.createDataset(ckanPackage);
                    // packageResponse = ckanService.updateDataset(existingDataset.getResult().getResults().get(0).getId(), categoryScheme); // TODO EDATOS-3460
                } else {
                    packageResponse = ckanService.createDataset(ckanPackage);
                }
                if (!packageResponse.isSuccess()) {
                    notificationService.createFailedNotification(urn, packageResponse.getError(), null);
                }
            } else {
                notificationService.createFailedNotification(urn, existingDataset.getError(), null);
            }
        } catch (HttpStatusCodeException e) {
            notificationError.manageCkanPublicationError(e, urn);
        }
    }

}
