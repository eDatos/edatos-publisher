package es.gobcan.istac.edatos.publisher.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import es.gobcan.istac.edatos.publisher.core.service.NotificationArgsService;

@Service
public final class NotificationArgsServiceImpl implements NotificationArgsService {

    private static final String UNKNOWN_ERROR_KEY = "notice.unknown.error.label";

    @Autowired
    private MessageSource messageSource;

    @Override
    public String[] argsByIdAndErrors(String id, Map<String, Object> ckanErrors, RestClientResponseException exception) {
        StringBuilder errors = new StringBuilder();
        if (ckanErrors == null || ckanErrors.isEmpty()) {
            if (exception != null) {
                String errorKey = messageSource.getMessage(UNKNOWN_ERROR_KEY, null, LocaleContextHolder.getLocale());
                errors.append("<li><strong>").append(errorKey).append("</strong>:").append(exception.getResponseBodyAsString()).append("</li>");
            }
        } else {
            for (String errorKey : ckanErrors.keySet()) {
                if (ckanErrors.get(errorKey) instanceof String) {
                    errors.append("<li><strong>").append(errorKey).append("</strong>:").append(ckanErrors.get(errorKey)).append("</li>");
                } else if (ckanErrors.get(errorKey) instanceof List<?>) {
                    for (Object errorValue : (List<?>) ckanErrors.get(errorKey)) {
                        if (errorValue instanceof String) {
                            errors.append("<li><strong>").append(errorKey).append("</strong>:").append(errorValue).append("</li>");
                        }
                    }
                }
            }
        }

        return new String[]{id, errors.toString()};
    }

    @Override
    public String[] argsByParamsAndErrors(Map<String, Object> ckanErrors, RestClientResponseException exception, String... params) {
        StringBuilder errors = new StringBuilder();
        if (ckanErrors == null || ckanErrors.isEmpty()) {
            if (exception != null) {
                String errorKey = messageSource.getMessage(UNKNOWN_ERROR_KEY, null, LocaleContextHolder.getLocale());
                errors.append("<li><strong>").append(errorKey).append("</strong>:").append(exception.getResponseBodyAsString()).append("</li>");
            }
        } else {
            for (String errorKey : ckanErrors.keySet()) {
                if (ckanErrors.get(errorKey) instanceof String) {
                    errors.append("<li><strong>").append(errorKey).append("</strong>:").append(ckanErrors.get(errorKey)).append("</li>");
                } else if (ckanErrors.get(errorKey) instanceof List<?>) {
                    for (Object errorValue : (List<?>) ckanErrors.get(errorKey)) {
                        if (errorValue instanceof String) {
                            errors.append("<li><strong>").append(errorKey).append("</strong>:").append(errorValue).append("</li>");
                        }
                    }
                }
            }
        }
        ArrayList<String> parameters = Stream.of(params).collect(Collectors.toCollection(ArrayList::new));
        parameters.add(errors.toString());
        return parameters.stream().toArray(n -> new String[n]);
    }
}
