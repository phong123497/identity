package com.dev.identityservice.dto.response;

import java.time.LocalDate;
import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    Set<RoleResponse> roles;
}