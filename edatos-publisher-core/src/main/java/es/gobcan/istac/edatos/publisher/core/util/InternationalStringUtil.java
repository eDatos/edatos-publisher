package es.gobcan.istac.edatos.publisher.core.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class InternationalStringUtil {

    private InternationalStringUtil() {

    }

    public static String generateTextI18n(MessageSource messageSource, String text, String[] args) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(text, args, locale);
    }
}
