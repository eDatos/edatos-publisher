package es.gobcan.istac.edatos.publisher.rest.internal.resources;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalOperationEntity;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.rest.common.dto.ExternalOperationDto;
import es.gobcan.istac.edatos.publisher.rest.common.mapper.ExternalOperationMapper;
import es.gobcan.istac.edatos.publisher.rest.common.util.PaginationUtil;

@RestController
@RequestMapping(ExternalOperationResource.BASE_URL)
public class ExternalOperationResource extends AbstractResource {

    public static final String ENTITY_NAME = "externalOperation";
    public static final String BASE_URL = "/api/external-operations";

    private final ExternalOperationService externalOperationService;
    private final ExternalOperationMapper externalOperationMapper;

    public ExternalOperationResource(ExternalOperationService externalOperationService, ExternalOperationMapper externalOperationMapper) {
        this.externalOperationService = externalOperationService;
        this.externalOperationMapper = externalOperationMapper;
    }

    @GetMapping
    @Timed
    @PreAuthorize("@secChecker.canAccessExternalOperation(authentication)")
    public ResponseEntity<List<ExternalOperationDto>> getExternalOperations(Pageable pageable, @RequestParam(required = false) String query) {
        Page<ExternalOperationDto> result = externalOperationService.find(query, pageable).map(externalOperationMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(result, BASE_URL);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @Timed
    @PutMapping
    @PreAuthorize("@secChecker.canUpdateExternalOperation(authentication)")
    public ResponseEntity<ExternalOperationDto> updateExternalOperation(@RequestBody ExternalOperationDto dto) {
        ExternalOperationEntity externalOperation = externalOperationMapper.toEntity(dto.getId(), dto.isEnabled(), dto.isNotificationsEnabled());
        externalOperation = externalOperationService.update(externalOperation);
        return ResponseEntity.ok(externalOperationMapper.toDto(externalOperation));
    }
}
