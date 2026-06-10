package com.nexora.user.profile.model;

import com.nexora.user.address.model.Address;
import com.nexora.user.preference.model.UserPreference;
import com.nexora.user.profile.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    private String userUid;

    @Builder.Default
    private String uid = UUID.randomUUID().toString();

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Email
    String email;

    @Size(min = 2, max = 50)
    private String lastName;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid Indian mobile number"
    )
    @Column(length = 10)
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profile_id")
    private List<Address> addresses;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_profile_id")
    private List<UserPreference> preferences;

    @Past
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Size(max = 500)
    private String bio;

    private String profileImageUrl;

    private String profileImageKey;

    @Builder.Default
    private Boolean deleted = false;

}