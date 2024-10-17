package com.dev.identityservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String username;

    // @Size(min = 8 , message = "INVALID_PASSWORD")
    private String password;
    private String firstName;
    private String lastName;

    // @DobConstraint(min = 10, message = "INVALID_DOB" )
    private LocalDate dob;

    @ManyToMany
    Set<Role> roles;
}
