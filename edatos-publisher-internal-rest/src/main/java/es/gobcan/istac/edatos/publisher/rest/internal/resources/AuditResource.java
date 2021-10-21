package es.gobcan.istac.edatos.publisher.rest.internal.resources;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.gobcan.istac.edatos.publisher.core.service.AuditEventService;
import es.gobcan.istac.edatos.publisher.rest.common.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping(AuditResource.BASE_URL)
public class AuditResource extends AbstractResource {

    public static final String BASE_URL = "/management/audits";

    private final AuditEventService auditEventService;

    public AuditResource(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    @GetMapping
    @PreAuthorize("@secChecker.canAccessAudit(authentication)")
    public ResponseEntity<List<AuditEvent>> find(Pageable pageable) {
        Page<AuditEvent> page = auditEventService.find(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URL);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(params = {"fromDate", "toDate"})
    @PreAuthorize("@secChecker.canAccessAudit(authentication)")
    public ResponseEntity<List<AuditEvent>> findByDates(@RequestParam(value = "fromDate") LocalDate fromDate, @RequestParam(value = "toDate") LocalDate toDate, Pageable pageable) {

        Page<AuditEvent> page = auditEventService.findByDates(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), toDate.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URL);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id:.+}")
    @PreAuthorize("@secChecker.canAccessAudit(authentication)")
    public ResponseEntity<AuditEvent> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(auditEventService.get(id));
    }
}
