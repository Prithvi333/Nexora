package com.nexora.user.preference.model;

import com.nexora.user.preference.enums.CurrencyType;
import com.nexora.user.preference.enums.Language;
import com.nexora.user.profile.model.UserProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    private String userUid;

    @Builder.Default
    private String uid = UUID.randomUUID().toString();

    @NotNull
    @Enumerated(EnumType.STRING)
    private Language language;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @ManyToOne
    UserProfile userProfile;

    @Builder.Default
    private Boolean defaultPreference = false;

    @Builder.Default
    private Boolean emailNotifications = true;

    @Builder.Default
    private Boolean smsNotifications = false;

    @Builder.Default
    private Boolean pushNotifications = true;
}