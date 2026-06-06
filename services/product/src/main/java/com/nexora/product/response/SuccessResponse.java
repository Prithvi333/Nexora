package com.nexora.product.response;

import java.time.LocalDateTime;

public record SuccessResponse(

        String message,

        int status,

        LocalDateTime timestamp

) {
}