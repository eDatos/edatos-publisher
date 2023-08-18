package es.gobcan.istac.edatos.publisher.service;

public interface ConsumerProcess<T> {

    boolean processPublicationEvent(T resource);
}
