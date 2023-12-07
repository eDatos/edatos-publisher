package es.gobcan.istac.edatos.publisher.core.service.impl;

import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.service.EDatosApisLocator;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;

@Service
public class ExternalOperationServiceImpl implements ExternalOperationService {

    private static final Logger     LOG = LoggerFactory.getLogger(ExternalCategoryServiceImpl.class);

    private final EDatosApisLocator eDatosApisLocator;

    public ExternalOperationServiceImpl(EDatosApisLocator eDatosApisLocator) {
        this.eDatosApisLocator = eDatosApisLocator;
    }

    @Override
    public Operation retrieveStatisticalOperationById(String statisticalOperationId) {
        LOG.debug("Making request to internal statistical operation Manager to GET operation by Id. OperationId: {}", statisticalOperationId);

        LOG.info("Making request to internal statistical operation Manager to GET operation by Id. OperationId: {}", statisticalOperationId);

        return eDatosApisLocator.operationsInternal().retrieveOperationById(statisticalOperationId);

    }
}
