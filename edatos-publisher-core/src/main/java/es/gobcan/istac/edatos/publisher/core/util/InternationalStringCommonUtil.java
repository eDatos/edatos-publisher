package es.gobcan.istac.edatos.publisher.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

public class InternationalStringCommonUtil {

    public InternationalStringCommonUtil() {

    }

    public static Map<String, List<String>> generateCkanInternationalStringMessage(MessageSource messageSource, List<String> messagesId, String[] args, List<String> internationalLanguages) {
        Map<String, List<String>> tags = new HashMap<>();

        for (String lang : internationalLanguages) {
            List<String> messagesByLang = new ArrayList<>();

            for (String messageId : messagesId) {
                String tag = generateTextI18n(messageSource, messageId, args, lang);
                messagesByLang.add(tag);
            }

            tags.put(lang, messagesByLang);
        }

        return tags;

    }

    public static Map<String, String> generateCkanInternationalStringMessage(MessageSource messageSource, String messageId, String[] args, List<String> internationalLanguages, String endText) {
        Map<String, String> texts = new HashMap<>();

        for (String lang : internationalLanguages) {
            String label = generateTextI18n(messageSource, messageId, args, lang);

            if (!StringUtils.isEmpty(endText)) {
                label += endText;
            }

            texts.put(lang, label);
        }

        return texts;
    }

    public static String generateTextI18n(MessageSource messageSource, String text, String[] args, String lang) {

        Locale locale = Locale.forLanguageTag(lang);

        return messageSource.getMessage(text, args, locale);
    }
}
