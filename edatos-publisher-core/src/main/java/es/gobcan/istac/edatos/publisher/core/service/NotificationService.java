package es.gobcan.istac.edatos.publisher.core.service;

import java.util.Map;

import org.springframework.web.client.RestClientResponseException;

public interface NotificationService {

    void createFailedNotification(String urn, Map<String, Object> ckanErrors, RestClientResponseException exception);
    void createNotification(String codeSubject, String messageCode, String[] args);
}
