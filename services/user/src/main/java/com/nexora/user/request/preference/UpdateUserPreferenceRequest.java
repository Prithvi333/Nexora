package com.nexora.user.request.preference;


public record UpdateUserPreferenceRequest(

        String userPreferenceUid,

        String language,

        String currency,

        Boolean emailNotifications,

        Boolean smsNotifications,

        Boolean pushNotifications
) {
}
