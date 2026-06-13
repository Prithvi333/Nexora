package com.nexora.user.address.model;

import com.nexora.user.profile.model.UserProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userUid;

    @Builder.Default
    private String uid = UUID.randomUUID().toString();

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Size(max = 150)
    private String addressLine;

    @NotBlank
    @Size(max = 50)
    private String city;

    @NotBlank
    @Size(max = 50)
    private String state;

    @NotBlank
    @Size(max = 50)
    private String country;

    @ManyToOne
    UserProfile userProfile;

    @NotBlank
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Invalid PIN code"
    )
    private String postalCode;

    @Builder.Default
    private Boolean defaultAddress = false;
}