package com.nexora.notification.exception.notification;

public class EmptyNotificationList extends RuntimeException {
    public EmptyNotificationList() {
        super("Empty notification list");
    }
}
