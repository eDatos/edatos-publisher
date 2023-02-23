package es.gobcan.istac.edatos.publisher.ckan.notification.error.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGenericResponse;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;

@Service
public class NotificationErrorImpl implements NotificationError {

    @Autowired
    private NotificationService notificationService;

    @Override
    public void manageCkanPublicationError(HttpStatusCodeException e, String urn) {
        CkanGenericResponse response;
        try {
            response = new ObjectMapper().readValue(e.getResponseBodyAsString(), CkanGenericResponse.class);
        } catch (JsonProcessingException ex) {
            response = new CkanGenericResponse();
        }

        notificationService.createFailedNotification(urn, response.getError(), e);

        if (e.getStatusCode().is5xxServerError() || response.getHelp() == null) {
            throw e;
        }
    }

}
