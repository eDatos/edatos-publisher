package es.gobcan.istac.edatos.publisher.rest.internal.resources;

import javax.validation.Valid;

import org.siemac.metamac.rest.notices.v1_0.domain.Receivers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import es.gobcan.istac.edatos.publisher.rest.common.dto.NotificationDto;

@RestController
@RequestMapping(NotificationResource.BASE_URL)
public class NotificationResource extends AbstractResource {

    public static final String BASE_URL = "/api/notifications";

    private final NotificationService notificationService;

    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Timed
    @PostMapping
    @PreAuthorize("@secChecker.canSendNotification(authentication)")
    public ResponseEntity<NotificationDto> send(@Valid @RequestBody NotificationDto notificationDto) {

        Receivers recivers = notificationService.createReceiversList(notificationDto.getReceivers());

        notificationService.createNotificationWithReceivers(notificationDto.getMessage(), notificationDto.getSubject(), recivers);

        return ResponseEntity.ok(notificationDto);
    }
}
