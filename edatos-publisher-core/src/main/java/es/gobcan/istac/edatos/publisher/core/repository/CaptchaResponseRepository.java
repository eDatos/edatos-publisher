package es.gobcan.istac.edatos.publisher.core.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.edatos.publisher.core.domain.CaptchaResponseEntity;

@Repository
public interface CaptchaResponseRepository extends JpaRepository<CaptchaResponseEntity, String> {

    public void deleteByCreatedDateBefore(Instant date);
}
