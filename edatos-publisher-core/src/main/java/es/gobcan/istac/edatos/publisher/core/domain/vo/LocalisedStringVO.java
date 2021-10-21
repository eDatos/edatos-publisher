package es.gobcan.istac.edatos.publisher.core.domain.vo;

import java.io.Serializable;
import java.util.Objects;

public class LocalisedStringVO implements Serializable {

    String label;
    String locale;

    public LocalisedStringVO() {
    }

    public LocalisedStringVO(String label, String locale) {
        this.label = label;
        this.locale = locale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalisedStringVO)) {
            return false;
        }
        LocalisedStringVO that = (LocalisedStringVO) o;
        return Objects.equals(label, that.label) && Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, locale);
    }

    @Override
    public String toString() {
        return locale + ": " + label;
    }
}
