package es.gobcan.istac.edatos.publisher.core.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.istac.edatos.publisher.core.service.AuditEventService;
import es.gobcan.istac.edatos.publisher.core.config.audit.AuditEventConverter;
import es.gobcan.istac.edatos.publisher.core.repository.PersistenceAuditEventRepository;

@Service
public class AuditEventServiceImpl implements AuditEventService {

    private final PersistenceAuditEventRepository persistenceAuditEventRepository;

    private final AuditEventConverter auditEventConverter;

    public AuditEventServiceImpl(PersistenceAuditEventRepository persistenceAuditEventRepository, AuditEventConverter auditEventConverter) {

        this.persistenceAuditEventRepository = persistenceAuditEventRepository;
        this.auditEventConverter = auditEventConverter;
    }

    @Override
    public Page<AuditEvent> find(Pageable pageable) {
        return persistenceAuditEventRepository.findAll(pageable).map(auditEventConverter::convertToAuditEvent);
    }

    @Override
    public Page<AuditEvent> findByDates(Instant fromDate, Instant toDate, Pageable pageable) {
        return persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable).map(auditEventConverter::convertToAuditEvent);
    }

    @Override
    public Optional<AuditEvent> get(Long id) {
        return Optional.ofNullable(persistenceAuditEventRepository.findOne(id)).map(auditEventConverter::convertToAuditEvent);
    }
}
