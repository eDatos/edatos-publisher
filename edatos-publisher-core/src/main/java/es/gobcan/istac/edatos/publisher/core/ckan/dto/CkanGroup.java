package es.gobcan.istac.edatos.publisher.core.ckan.dto;

public class CkanGroup {

    private String name;
    private String id;

    public CkanGroup() {}

    public CkanGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
