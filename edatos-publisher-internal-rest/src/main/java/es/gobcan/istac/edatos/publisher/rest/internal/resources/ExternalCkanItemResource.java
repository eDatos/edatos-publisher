package es.gobcan.istac.edatos.publisher.rest.internal.resources;

import com.codahale.metrics.annotation.Timed;
import es.gobcan.istac.edatos.publisher.core.config.audit.AuditEventPublisher;
import es.gobcan.istac.edatos.publisher.core.repository.ExternalCkanItemRepository;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCkanItemService;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalCkanItemDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalCkanItemMapper;
import es.gobcan.istac.edatos.publisher.rest.common.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ExternalCkanItemResource.BASE_URL)
public class ExternalCkanItemResource extends AbstractResource {

    public static final String BASE_URL = "/api/external-resources";

    private final ExternalCkanItemRepository externalCkanItemRepository;
    private final ExternalCkanItemService externalCkanItemService;
    private final ExternalCkanItemMapper externalCkanItemMapper;
    private final NotificationService notificationService;
    private final AuditEventPublisher auditPublisher;

    public ExternalCkanItemResource(ExternalCkanItemRepository externalCkanItemRepository, ExternalCkanItemService externalCkanItemService, ExternalCkanItemMapper externalCkanItemMapper, AuditEventPublisher auditPublisher,
        NotificationService notificationService) {
        this.externalCkanItemRepository = externalCkanItemRepository;
        this.externalCkanItemService = externalCkanItemService;
        this.externalCkanItemMapper = externalCkanItemMapper;
        this.auditPublisher = auditPublisher;
        this.notificationService = notificationService;
    }


    @Timed
    @GetMapping
    //@PreAuthorize("@secChecker.canAccessUser(authentication)")
    public ResponseEntity<List<ExternalCkanItemDto>> find(Pageable pageable, String query) {
        Page<ExternalCkanItemDto> page = externalCkanItemService.find(pageable, query).map(externalCkanItemMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, BASE_URL);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
