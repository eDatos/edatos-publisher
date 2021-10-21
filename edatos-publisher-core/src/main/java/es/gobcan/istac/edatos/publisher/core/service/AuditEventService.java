package es.gobcan.istac.edatos.publisher.core.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditEventService {

    Page<AuditEvent> find(Pageable pageable);

    Page<AuditEvent> findByDates(Instant fromDate, Instant toDate, Pageable pageable);

    Optional<AuditEvent> get(Long id);
}
