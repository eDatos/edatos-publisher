package es.gobcan.istac.edatos.publisher.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;
import org.springframework.context.MessageSource;

import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;

public class InternationalStringUtil {

    private InternationalStringUtil() {

    }

    public static String generateTextI18n(MessageSource messageSource, String text, String[] args) {

        Locale locale = Locale.forLanguageTag(Constants.CKAN_LANGUAGE);

        return messageSource.getMessage(text, args, locale);
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
}
