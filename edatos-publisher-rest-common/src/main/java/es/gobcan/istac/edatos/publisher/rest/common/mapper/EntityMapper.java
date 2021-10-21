package es.gobcan.istac.edatos.publisher.rest.common.mapper;

import java.util.List;

interface EntityMapper<D, E> {

    D toDto(E entity);

    E toEntity(D dto);

    List<E> toEntities(List<D> dtos);

    List<D> toDtos(List<E> entities);
}
