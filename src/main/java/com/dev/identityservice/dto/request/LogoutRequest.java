package com.dev.identityservice.dto.request;

import lombok.Data;

@Data
public class LogoutRequest {
    private String token;
}
