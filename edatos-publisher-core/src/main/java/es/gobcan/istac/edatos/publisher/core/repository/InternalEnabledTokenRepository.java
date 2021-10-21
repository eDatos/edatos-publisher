package es.gobcan.istac.edatos.publisher.core.repository;

import es.gobcan.istac.edatos.publisher.core.domain.InternalEnabledTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface InternalEnabledTokenRepository extends JpaRepository<InternalEnabledTokenEntity, String> {

    void deleteByExpirationDateBefore(Instant date);
    boolean existsByToken(String token);
}
