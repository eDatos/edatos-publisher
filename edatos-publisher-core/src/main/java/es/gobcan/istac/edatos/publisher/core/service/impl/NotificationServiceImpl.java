package es.gobcan.istac.edatos.publisher.core.service.impl;

import es.gobcan.istac.edatos.publisher.core.service.EDatosApisLocator;
import es.gobcan.istac.edatos.publisher.core.service.NotificationArgsService;
import es.gobcan.istac.edatos.publisher.core.service.NotificationService;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.Locale;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EDatosApisLocator eDatosApisLocator;

    @Autowired
    private NotificationArgsService notificationArgsService;

    @Override
    public void createFailedNotification(String urn, Map<String, Object> ckanErrors, RestClientResponseException exception) {
        String codeSubject = "notice.subject.failed_publication.text";
        String messageCode = "notice.message.failed_publication.text";

        String[] args = notificationArgsService.argsByIdAndErrors(urn, ckanErrors, exception);

        createNotification(codeSubject, messageCode, args);
    }

    @Override
    public void createNotification(String codeSubject, String messageCode, String[] args) {
        try {
            Notice notice = createNotice(codeSubject, messageCode, args);

            eDatosApisLocator.getNoticesRestInternalFacadeV10().createNotice(notice);
        } catch (Exception e) {
            log.debug("Error al enviar notificacion : {}", e);
        }
    }

    private Notice createNotice(String codeSubject, String messageCode, String[] args) {
        String subject = generateTextI18n(codeSubject, args);
        Message message = createMessage(messageCode, args);

        // @formatter:off
        return NoticeBuilder.notification()
            .withMessages(message)
            .withSendingApplication(MetamacApplicationsEnum.GESTOR_PUBLICACIONES.getName()) 
            .withSubject(subject)
            .build();
        // @formatter:on
    }

    private Message createMessage(String messageCode, String[] args) {
        String localisedMessage = generateTextI18n(messageCode, args);

        // @formatter:off
        return  MessageBuilder.message()
            .withText(localisedMessage)
            .build();
        // @formatter:on
    }

    private String generateTextI18n(String text, String[] args) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(text, args, locale);
    }
}
