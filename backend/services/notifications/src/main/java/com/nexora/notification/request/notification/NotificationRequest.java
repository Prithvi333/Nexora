package com.nexora.notification.request.notification;

import com.nexora.notification.kafka.enums.EventType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record NotificationRequest(
        @NotBlank(message = "User UID is required")
        String userUid,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Message is required")
        @Size(
                min = 1,
                max = 1000,
                message = "Message must be between 1 and 1000 characters"
        )
        String message,

        @NotNull(message = "Event type is required")
        EventType eventType

) {
}
