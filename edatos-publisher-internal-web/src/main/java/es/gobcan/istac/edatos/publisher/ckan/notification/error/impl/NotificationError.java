package es.gobcan.istac.edatos.publisher.ckan.notification.error.impl;

import org.springframework.web.client.HttpStatusCodeException;

public interface NotificationError {

    void manageCkanPublicationError(String codeSubject, String messageCode, HttpStatusCodeException e, String... params);

}
