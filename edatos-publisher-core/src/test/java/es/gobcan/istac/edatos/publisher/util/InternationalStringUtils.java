package es.gobcan.istac.edatos.publisher.util;

import java.util.Map;

import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;
import es.gobcan.istac.edatos.publisher.core.domain.vo.LocalisedStringVO;

public class InternationalStringUtils {
    public static InternationalStringVO createIntStr(Map<String, String> text) {
        InternationalStringVO intStr = new InternationalStringVO();
        for (Map.Entry<String, String> entry : text.entrySet()) {
            LocalisedStringVO locStr = new LocalisedStringVO();
            locStr.setLocale(entry.getKey());
            locStr.setLabel(entry.getValue());
            intStr.addText(locStr);
        }
        return intStr;
    }

    public static InternationalStringVO createIntStr(String locale, String label) {
        InternationalStringVO target = new InternationalStringVO();
        LocalisedStringVO localisedString = new LocalisedStringVO();
        localisedString.setLocale(locale);
        localisedString.setLabel(label);
        target.addText(localisedString);
        return target;
    }
}
