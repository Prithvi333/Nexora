package com.nexora.notification.notification.model;

import com.nexora.notification.kafka.enums.EventType;
import com.nexora.notification.notification.enums.NotificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User Uid is required")
    @Size(max = 100, message = "User Uid cannot exceed 100 characters")
    @Column(nullable = false)
    private String userUid;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Subject is required")
    @Size(min = 3, max = 255,
            message = "Subject must be between 3 and 255 characters")
    @Column(nullable = false)
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(min = 5, max = 5000,
            message = "Message must be between 5 and 5000 characters")
    @Column(nullable = false, length = 5000)
    private String message;

    @NotNull(message = "Status is required")
    private NotificationStatus notificationStatus;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


}