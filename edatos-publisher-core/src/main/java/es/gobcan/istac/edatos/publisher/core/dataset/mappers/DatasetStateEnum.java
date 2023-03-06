package es.gobcan.istac.edatos.publisher.core.dataset.mappers;

import java.io.Serializable;

/**
 * Enum for NoticesRoleEnum
 */
public enum DatasetStateEnum implements Serializable {
    ACTIVE, DELETE;

    /**
     */
    private DatasetStateEnum() {
    }

    public String getName() {
        return name().toLowerCase();
    }
}
