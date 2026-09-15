package com.nexora.ai.dto.users;

import lombok.Builder;

@Builder
public record UserPreferenceResponse(


        String userPreferenceUid,

        Language language,

        CurrencyType currency,

        Boolean emailNotifications,

        Boolean smsNotifications,

        Boolean defaultPreference,

        Boolean pushNotifications

) {
}
