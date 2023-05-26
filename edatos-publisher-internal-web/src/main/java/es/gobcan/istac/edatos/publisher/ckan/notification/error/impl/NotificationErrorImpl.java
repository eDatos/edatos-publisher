package es.gobcan.istac.edatos.publisher.ckan.notification.error.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGenericResponse;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;

@Service
public class NotificationErrorImpl implements NotificationError {

    private final Logger log = LoggerFactory.getLogger(NotificationErrorImpl.class);

    @Autowired
    private NotificationService notificationService;

    @Override
    public void manageCkanPublicationError(String codeSubject, String messageCode, HttpStatusCodeException e, String... params) {

        CkanGenericResponse response;
        try {
            response = new ObjectMapper().readValue(e.getResponseBodyAsString(), CkanGenericResponse.class);
        } catch (JsonProcessingException ex) {
            log.error(e.getMessage());
            response = new CkanGenericResponse();
        }

        notificationService.createFailedNotification(codeSubject, messageCode, response.getError(), e, params);

        if (e.getStatusCode().is5xxServerError() || response.getHelp() == null) {
            throw e;
        }
    }
}
