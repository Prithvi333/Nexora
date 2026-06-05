package com.nexora.auth.response;

import java.time.LocalDateTime;

public record SuccessResponse(

        String message,

        int status,

        LocalDateTime timestamp

) {
}