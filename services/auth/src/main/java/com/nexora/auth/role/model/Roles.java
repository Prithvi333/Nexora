package com.nexora.auth.role.model;

import com.nexora.auth.user.model.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Builder
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<Users> users;
}
