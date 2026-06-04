package com.nexora.gateway.response;

public record TokenValidationResponse(
        Boolean valid,
        String userName,
        String roles) {
}
