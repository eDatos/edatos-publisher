package es.gobcan.istac.edatos.publisher.service;

import es.gobcan.istac.edatos.publisher.service.impl.PublicationConsumerProcessResult;

public interface ConsumerProcess<T> {

    PublicationConsumerProcessResult processPublicationEvent(T resource);
}
