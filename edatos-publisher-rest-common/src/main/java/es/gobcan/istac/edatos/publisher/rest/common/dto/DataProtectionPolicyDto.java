package es.gobcan.istac.edatos.publisher.rest.common.dto;

import org.siemac.edatos.core.common.dto.AuditableDto;

import es.gobcan.istac.edatos.publisher.rest.common.dto.interfaces.Identifiable;

public class DataProtectionPolicyDto extends AuditableDto implements Identifiable {

    private static final long serialVersionUID = 2729518661182841945L;

    private Long id;
    private InternationalStringDto value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InternationalStringDto getValue() {
        return value;
    }

    public void setValue(InternationalStringDto value) {
        this.value = value;
    }
}
