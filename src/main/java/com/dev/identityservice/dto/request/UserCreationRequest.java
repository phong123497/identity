package com.dev.identityservice.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Size;

import com.dev.identityservice.validator.DobConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    private String username;

    @Size(min = 3, message = "INVALID_PASSWORD")
    private String password;

    private String firstName;
    private String lastName;

    @DobConstraint(min = 10, message = "INVALID_DOB")
    private LocalDate dob;

    List<String> roles;
}
