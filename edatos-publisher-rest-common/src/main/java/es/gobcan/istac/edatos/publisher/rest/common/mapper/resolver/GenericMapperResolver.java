package es.gobcan.istac.edatos.publisher.rest.common.mapper.resolver;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.rest.common.dto.interfaces.Identifiable;

@Component
public class GenericMapperResolver {

    @PersistenceContext
    EntityManager em;

    @ObjectFactory
    public <E, D extends Identifiable> E resolver(D dto, @TargetType Class<E> type) {
        try {
            return (dto != null && dto.getId() != null) ? em.find(type, dto.getId()) : type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e); // TODO EDATOS-3124 Miguel check this!
        }
    }

}
