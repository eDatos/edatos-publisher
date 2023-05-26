package es.gobcan.istac.edatos.publisher.core.service;

import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;

public interface ExternalOperationService {

    public Operation retrieveStatisticalOperationById(String statisticalOperationId);

}
