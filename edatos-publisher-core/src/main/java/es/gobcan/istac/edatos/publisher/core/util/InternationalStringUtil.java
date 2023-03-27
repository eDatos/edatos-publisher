package es.gobcan.istac.edatos.publisher.core.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;

public class InternationalStringUtil {

    private InternationalStringUtil() {

    }

    public static String generateTextI18n(MessageSource messageSource, String text, String[] args) {

        Locale locale = Locale.forLanguageTag(Constants.CKAN_LANGUAGE);

        return messageSource.getMessage(text, args, locale);
    }
}
