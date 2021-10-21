package es.gobcan.istac.edatos.publisher.rest.common.dto;

import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;

import es.gobcan.istac.edatos.publisher.rest.common.dto.interfaces.AbstractVersionedDto;

public class ExternalItemDto extends AbstractVersionedDto {

    protected String code;
    protected String urn;
    protected InternationalStringDto name;
    protected TypeExternalArtefactsEnum type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public InternationalStringDto getName() {
        return name;
    }

    public void setName(InternationalStringDto name) {
        this.name = name;
    }

    public TypeExternalArtefactsEnum getType() {
        return type;
    }

    public void setType(TypeExternalArtefactsEnum type) {
        this.type = type;
    }
}
