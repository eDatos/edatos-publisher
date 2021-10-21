package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.dto.AuditableDto;

import es.gobcan.istac.edatos.publisher.rest.common.dto.interfaces.Identifiable;

public class StructuralResourcesTreeDto extends AuditableDto implements Identifiable {

    private Long id;
    private String code;
    private InternationalStringDto name;
    private String type;
    private long subscribers;
    private List<StructuralResourcesTreeDto> children = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InternationalStringDto getName() {
        return name;
    }

    public void setName(InternationalStringDto name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<StructuralResourcesTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<StructuralResourcesTreeDto> children) {
        this.children = children;
    }

    public long getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(long subscribers) {
        this.subscribers = subscribers;
    }
}
