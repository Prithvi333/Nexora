package com.nexora.user.response.preference;

import com.nexora.user.preference.enums.CurrencyType;
import com.nexora.user.preference.enums.Language;
import lombok.Builder;

@Builder
public record UserPreferenceResponse(


        String userPreferenceUid,

        Language language,

        CurrencyType currency,

        Boolean emailNotifications,

        Boolean smsNotifications,

        Boolean pushNotifications

) {
}
