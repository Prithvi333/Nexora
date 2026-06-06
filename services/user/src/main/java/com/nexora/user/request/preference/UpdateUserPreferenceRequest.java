package com.nexora.user.request.preference;

import com.nexora.user.preference.enums.CurrencyType;
import com.nexora.user.preference.enums.Language;

public record UpdateUserPreferenceRequest(

        String userPreferenceUid,

        String language,

        String currency,

        Boolean emailNotifications,

        Boolean smsNotifications,

        Boolean pushNotifications
) {
}
