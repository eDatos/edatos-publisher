package es.gobcan.istac.edatos.publisher.rest.common.dto;

import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;

public class ExternalCkanItemDto extends ExternalItemDto {

    private CkanState ckanState;
    private String kafkaMessage;

    public CkanState getCkanState() {
        return ckanState;
    }

    public void setCkanState(CkanState ckanState) {
        this.ckanState = ckanState;
    }

    public String getKafkaMessage() {
        return kafkaMessage;
    }

    public void setKafkaMessage(String kafkaMessage) {
        this.kafkaMessage = kafkaMessage;
    }
}
