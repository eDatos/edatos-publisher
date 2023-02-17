package es.gobcan.istac.edatos.publisher.core.service;

import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

public interface NotificationArgsService {

    String[] argsByIdAndErrors(String id, Map<String, Object> ckanErrors, RestClientResponseException exception);
}
