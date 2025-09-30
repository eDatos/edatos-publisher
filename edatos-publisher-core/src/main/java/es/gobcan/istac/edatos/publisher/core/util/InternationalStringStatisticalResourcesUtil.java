package es.gobcan.istac.edatos.publisher.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

import es.gobcan.istac.edatos.publisher.core.util.markdown.MarkdownCkan;

public class InternationalStringStatisticalResourcesUtil extends InternationalStringCommonUtil {

    private InternationalStringStatisticalResourcesUtil() {

    }

    public static List<InternationalStringItemAvro> getInternationalStringItemAvroFromGenericRecordAvro(InternationalStringAvro internationalStringAvro) {
        List<InternationalStringItemAvro> target = new ArrayList<>();
        List<InternationalStringItemAvro> source = internationalStringAvro.getLocalisedStrings();

        for (Object element : source) {
            GenericRecord recordInternationalStringAvro = (GenericRecord) element;
            InternationalStringItemAvro item = (InternationalStringItemAvro) SpecificData.get().deepCopy(InternationalStringItemAvro.getClassSchema(), recordInternationalStringAvro);
            target.add(item);
        }
        return target;
    }

    public static InternationalStringAvro getInternationalStringAvroFromGenericRecordAvro(InternationalStringAvro source) {
        InternationalStringAvro target = new InternationalStringAvro();
        List<InternationalStringItemAvro> items = getInternationalStringItemAvroFromGenericRecordAvro(source);
        target.setLocalisedStrings(items);
        return target;
    }

    private static Map<String, String> getInternationalStringToCkanInternationalString(InternationalStringAvro internationalStringAvro, String labelStringToAdd, boolean markDownRequired) {
        Map<String, String> data = new HashMap<>();

        List<InternationalStringItemAvro> source = internationalStringAvro.getLocalisedStrings();

        for (Object element : source) {
            GenericRecord recordInternationalStringAvro = (GenericRecord) element;
            InternationalStringItemAvro item = (InternationalStringItemAvro) SpecificData.get().deepCopy(InternationalStringItemAvro.getClassSchema(), recordInternationalStringAvro);

            String label = item.getLabel();
            if (!StringUtils.isEmpty(labelStringToAdd)) {
                label += " " + labelStringToAdd;
            }

            data.put(item.getLocale(), markDownRequired ? MarkdownCkan.stringHtmlToMarkdown(label) : label);
        }

        return data;

    }

    public static Map<String, String> getStatisticalResourcesInternationalStringTitleToCkanInternationalString(InternationalStringAvro internationalStringAvro, String endText) {
        return getInternationalStringToCkanInternationalString(internationalStringAvro, endText, false);

    }

    public static Map<String, String> getSrmInternationalStringToCkanInternationalStringMarkDown(InternationalStringAvro internationalStringAvro, String endText) {
        return getInternationalStringToCkanInternationalString(internationalStringAvro, endText, true);
    }

    public static String getDefaultLocalisedStringFromInternationalStringItemAvro(List<InternationalStringItemAvro> localisedString, String defaultLanguage) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(defaultLanguage))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(defaultLanguage)).findFirst().get().getLabel();
        }
        return null;
    }
}
