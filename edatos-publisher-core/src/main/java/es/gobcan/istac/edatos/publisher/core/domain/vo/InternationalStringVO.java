package es.gobcan.istac.edatos.publisher.core.domain.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InternationalStringVO implements Serializable {

    private final Set<LocalisedStringVO> texts = new HashSet<>();

    public Set<LocalisedStringVO> getTexts() {
        return texts;
    }

    public void addText(LocalisedStringVO textElement) {
        getTexts().add(textElement);
    }

    public void removeText(LocalisedStringVO textElement) {
        getTexts().remove(textElement);
    }

    public void removeAllTexts() {
        getTexts().clear();
    }

    public LocalisedStringVO getLocalisedLabelEntity(String locale) {
        if (locale == null) {
            return null;
        }
        for (LocalisedStringVO localstr : getTexts()) {
            if (locale.equalsIgnoreCase(localstr.getLocale())) {
                return localstr;
            }
        }
        return null;
    }

    public String getLocalisedLabel(String locale) {
        if (locale == null) {
            return null;
        }
        for (LocalisedStringVO localstr : getTexts()) {
            if (locale.equalsIgnoreCase(localstr.getLocale())) {
                return localstr.getLabel();
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InternationalStringVO)) {
            return false;
        }
        InternationalStringVO that = (InternationalStringVO) o;
        return Objects.equals(texts, that.texts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texts);
    }

    @Override
    public String toString() {
        return texts.toString();
    }
}
