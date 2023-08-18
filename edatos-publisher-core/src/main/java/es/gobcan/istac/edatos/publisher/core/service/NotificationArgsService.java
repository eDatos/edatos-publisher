package es.gobcan.istac.edatos.publisher.core.service;

import java.util.Map;

import org.springframework.web.client.RestClientResponseException;

public interface NotificationArgsService {

    String[] argsByIdAndErrors(String id, Map<String, Object> ckanErrors, RestClientResponseException exception);
    public String[] argsByParamsAndErrors(Map<String, Object> ckanErrors, RestClientResponseException exception, String... params);
}
