package com.nexora.user.response;


import java.time.LocalDateTime;

public record SuccessResponse<T>(

        T message,

        int status,

        LocalDateTime timestamp

) {
}