package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.io.Serializable;

import es.gobcan.istac.edatos.publisher.rest.common.dto.interfaces.AbstractVersionedAndAuditingDto;

public class FavoriteDto extends AbstractVersionedAndAuditingDto implements Serializable {

    private ExternalUserAccountBaseDto externalUser;
    private CategoryDto category;
    private ExternalOperationDto externalOperation;

    public ExternalUserAccountBaseDto getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(ExternalUserAccountBaseDto externalUser) {
        this.externalUser = externalUser;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public ExternalOperationDto getExternalOperation() {
        return externalOperation;
    }

    public void setExternalOperation(ExternalOperationDto externalOperation) {
        this.externalOperation = externalOperation;
    }
}
