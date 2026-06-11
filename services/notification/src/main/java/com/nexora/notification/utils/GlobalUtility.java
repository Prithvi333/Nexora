package com.nexora.notification.utils;

import com.nexora.notification.kafka.enums.EventType;
import com.nexora.notification.notification.enums.NotificationStatus;
import com.nexora.notification.notification.model.Notification;
import com.nexora.notification.response.notification.NotificationResponse;
import com.nexora.notification.security.UserPrinciple;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

public class GlobalUtility {

    public static Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        pageSize = pageSize == null ? 5 : pageSize;
        pageNo = pageNo == null ? 0 : pageNo;
        Sort sort = direction == null ? Sort.by(sortBy).ascending() : direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNo, pageSize, sort);
    }

    public static Notification convertFRomArgsToNotification(String to, String body, String userUid, EventType eventType) {
        return Notification.builder().subject("Notification").email(to)
                .message(body)
                .userUid(userUid)
                .notificationStatus(NotificationStatus.SENT)
                .eventType(eventType)
                .build();
    }

    public static NotificationResponse convertFromNotificationToNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .userId(notification.getUserUid())
                .email(notification.getEmail())
                .subject(notification.getSubject())
                .status(notification.getNotificationStatus())
                .eventType(notification.getEventType())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static UserPrinciple getLoggedInUserDetails() {
        return (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
