package es.gobcan.istac.edatos.publisher.rest.common.dto.interfaces;

import java.io.Serializable;

public abstract class AbstractVersionedDto implements Identifiable, Versionable, Serializable, Cloneable {

    private Long id;
    private Long optLock;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getOptLock() {
        return optLock;
    }

    @Override
    public void setOptLock(Long optLock) {
        this.optLock = optLock;
    }
}

