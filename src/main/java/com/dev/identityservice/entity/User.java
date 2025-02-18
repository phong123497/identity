package com.dev.identityservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

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
