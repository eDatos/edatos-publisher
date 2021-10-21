package es.gobcan.istac.edatos.publisher.rest.common.dto;

public class ExternalDatasetDto extends ExternalItemDto {

    private String externalOperationUrn;

    public String getExternalOperationUrn() {
        return externalOperationUrn;
    }

    public void setExternalOperationUrn(String externalOperationUrn) {
        this.externalOperationUrn = externalOperationUrn;
    }
}
