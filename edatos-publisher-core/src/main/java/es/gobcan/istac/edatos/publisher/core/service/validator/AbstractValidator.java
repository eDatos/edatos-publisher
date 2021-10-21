package es.gobcan.istac.edatos.publisher.core.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class AbstractValidator<E> {

    @Autowired
    protected PlatformTransactionManager platformTransactionManager;

    protected E getOriginalEntity(TransactionCallback<E> callback) {
        TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        template.setReadOnly(true);
        return template.execute(callback);
    }
}
