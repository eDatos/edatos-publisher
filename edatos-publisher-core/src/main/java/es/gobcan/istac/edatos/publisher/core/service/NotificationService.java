package es.gobcan.istac.edatos.publisher.core.service;

import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

public interface NotificationService {

    void createFailedDatasetPublicationNotification(String urn, Map<String, Object> ckanErrors, RestClientResponseException exception);
    void createNotification(String codeSubject, String messageCode, String[] args);
}
