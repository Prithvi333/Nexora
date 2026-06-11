package com.nexora.notification.notification.service;

import com.nexora.notification.kafka.enums.EventType;
import com.nexora.notification.exception.notification.EmptyNotificationList;
import com.nexora.notification.notification.enums.NotificationStatus;
import com.nexora.notification.notification.model.Notification;
import com.nexora.notification.notification.repository.NotificationRepository;
import com.nexora.notification.request.notification.NotificationRequest;
import com.nexora.notification.response.notification.NotificationResponse;
import com.nexora.notification.utils.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        logger.info("Entering sendNotification for userUid: {} and email: {}", notificationRequest.userUid(), notificationRequest.email());
        emailNotificationService.sendEmail(notificationRequest.email(), notificationRequest.message(), notificationRequest.userUid());
        logger.info("Email notification sent to: {}", notificationRequest.email());
        Notification notification = GlobalUtility.convertFRomArgsToNotification(notificationRequest.email(), notificationRequest.message(), notificationRequest.userUid(), notificationRequest.eventType());
        notificationRepository.save(notification);
        logger.info("Notification saved successfully for userUid: {}", notificationRequest.userUid());
    }

    @Override
    public List<NotificationResponse> getAllNotification(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering getAllNotification with pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);

        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        if (notifications.isEmpty()) {
            logger.warn("No notifications found");
            throw new EmptyNotificationList();
        }
        logger.info("Fetched {} notifications successfully", notifications.getContent().size());
        return notifications.stream().map(GlobalUtility::convertFromNotificationToNotificationResponse).toList();
    }

}