package com.nexora.notification.notification.service;

import com.nexora.notification.exception.notification.EmptyNotificationList;
import com.nexora.notification.notification.enums.NotificationEventType;
import com.nexora.notification.notification.enums.NotificationStatus;
import com.nexora.notification.notification.model.Notification;
import com.nexora.notification.notification.repository.NotificationRepository;
import com.nexora.notification.response.notification.NotificationResponse;
import com.nexora.notification.utils.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendNotification(String to, String body, String userUid, NotificationEventType eventType) {
        emailNotificationService.sendEmail(to, body, userUid);
        Notification notification = GlobalUtility.convertFRomArgsToNotification(to, body, userUid, eventType, NotificationStatus.SENT);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getAllNotification(Integer pageNo, Integer pageSize, String sortBy, String direction) {

        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        if (notifications.isEmpty()) {
            throw new EmptyNotificationList();
        }
        return notifications.stream().map(GlobalUtility::convertFromNotificationToNotificationResponse).toList();
    }
}
