package es.gobcan.istac.edatos.publisher.core.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.edatos.publisher.core.domain.DisabledTokenEntity;

@Repository
public interface DisabledTokenRepository extends JpaRepository<DisabledTokenEntity, String> {

    public void deleteByExpirationDateBefore(Instant date);
}
