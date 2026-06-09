package com.nexora.notification.kafka.event;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderCreatedEvent(

        @NotNull
        Long orderUid,

        @NotBlank
        String userId,

        @Email
        String email,

        @NotNull
        Double amount
) {
}
