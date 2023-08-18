package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.io.Serializable;

public class LocalisedStringDto implements Serializable {

    private String label;
    private String locale;

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
