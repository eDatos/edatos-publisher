package es.gobcan.istac.edatos.publisher.core.service;

import java.util.Map;

import org.springframework.web.client.RestClientResponseException;

public interface NotificationService {

    void createNotification(String codeSubject, String messageCode, String[] args);
    void createFailedNotification(String codeSubject, String messageCode, Map<String, Object> ckanErrors, RestClientResponseException exception, String... params);
}
