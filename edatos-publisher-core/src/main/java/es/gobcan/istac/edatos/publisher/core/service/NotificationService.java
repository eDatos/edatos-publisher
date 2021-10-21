package es.gobcan.istac.edatos.publisher.core.service;

import es.gobcan.istac.edatos.publisher.core.domain.ExternalUserEntity;
import es.gobcan.istac.edatos.publisher.core.domain.FavoriteEntity;

import java.util.List;

import org.siemac.metamac.rest.notices.v1_0.domain.Receivers;

public interface NotificationService {

    void createNewExternalUserAccountNotification(ExternalUserEntity externalUserEntity);
    void createDeleteExternalUserAccountNotification(ExternalUserEntity externalUserEntity);
    void createChangePasswordExternaluserAccountNotification(ExternalUserEntity externalUserEntity);
    void createResetPasswordExternaluserAccountNotification(ExternalUserEntity externalUserEntity);

    void createNoticeOfSusbcritionsJob(List<FavoriteEntity> listFavorites);

    void createNotificationWithReceivers(String message, String subject, Receivers receivers);
    Receivers createReceiversList(List<String> emails);
}
