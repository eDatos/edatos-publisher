package es.gobcan.istac.edatos.publisher.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.srm.core.stream.message.InternationalStringAvro;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;

import es.gobcan.istac.edatos.publisher.core.util.markdown.MarkdownCkan;

public class InternationalStringSrmUtil extends InternationalStringCommonUtil {

    private InternationalStringSrmUtil() {
    }

    private static Map<String, String> getSrmInternationalStringToCkanInternationalString(InternationalStringAvro internationalStringAvro, String endText, boolean markDownRequired) {
        Map<String, String> data = new HashMap<>();

        List<LocalisedStringAvro> source = internationalStringAvro.getLocalisedStrings();

        for (LocalisedStringAvro element : source) {

            String label = element.getLabel();
            if (!StringUtils.isEmpty(endText)) {
                label += endText;
            }

            data.put(element.getLocale(), markDownRequired ? MarkdownCkan.stringHtmlToMarkdown(label) : label);
        }

        return data;

    }

    public static Map<String, String> getSrmInternationalStringToCkanInternationalString(InternationalStringAvro internationalStringAvro, String endText) {
        return getSrmInternationalStringToCkanInternationalString(internationalStringAvro, endText, false);
    }

    public static Map<String, String> getSrmInternationalStringToCkanInternationalStringMarkDown(InternationalStringAvro internationalStringAvro, String endText) {
        return getSrmInternationalStringToCkanInternationalString(internationalStringAvro, endText, true);
    }

    public static String getDefaultLocalisedStringFromLocalisedStringAvro(List<LocalisedStringAvro> localisedString, String defaultLanguage) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(defaultLanguage))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(defaultLanguage)).findFirst().get().getLabel();
        }
        return null;
    }
}
