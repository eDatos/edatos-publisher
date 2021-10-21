package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class InternationalStringDto implements Serializable {

    private final Set<LocalisedStringDto> texts = new HashSet<>();

    public Set<LocalisedStringDto> getTexts() {
        return this.texts;
    }

    public void addText(LocalisedStringDto textElement) {
        this.getTexts().add(textElement);
    }

    public void removeText(LocalisedStringDto textElement) {
        this.getTexts().remove(textElement);
    }

    public void removeAllTexts() {
        this.getTexts().clear();
    }

    public LocalisedStringDto getLocalised(String locale) {
        if (locale == null) {
            return null;
        } else {
            Iterator<LocalisedStringDto> var2 = this.getTexts().iterator();
            LocalisedStringDto localstr;
            do {
                if (!var2.hasNext()) {
                    return null;
                }
                localstr = var2.next();
            } while (!locale.equalsIgnoreCase(localstr.getLocale()));
            return localstr;
        }
    }

    public String getLocalisedLabel(String locale) {
        LocalisedStringDto localstr = this.getLocalised(locale);
        return localstr != null ? localstr.getLabel() : null;
    }
}
