package com.nexora.auth.role.model;

import com.nexora.auth.user.model.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private String uid = UUID.randomUUID().toString();

    private String roleName;

    @Builder.Default
    private Boolean enable = true;

    @ManyToMany(mappedBy = "roles")
    private Set<Users> users;
}
