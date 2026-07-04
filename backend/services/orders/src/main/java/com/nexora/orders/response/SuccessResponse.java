package com.nexora.orders.response;

import java.time.LocalDateTime;

public record SuccessResponse(

        String message,

        int status,

        LocalDateTime timestamp

) {
}