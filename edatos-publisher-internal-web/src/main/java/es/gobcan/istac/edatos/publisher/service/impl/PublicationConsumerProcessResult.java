package es.gobcan.istac.edatos.publisher.service.impl;

import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CkanState;

public class PublicationConsumerProcessResult {

    boolean   ok;
    String    ckanId;
    CkanState status;

    public CkanState getStatus() {
        return status;
    }

    PublicationConsumerProcessResult(boolean ok, String ckanId, CkanState status) {
        this.ok = ok;
        this.ckanId = ckanId;
        this.status = status;
    }

    public boolean isOk() {
        return ok;
    }

    public String getCkanId() {
        return ckanId;
    }
}
