package com.dev.identityservice.dto.request;

import lombok.Data;

@Data
public class RefreshRequest {
    private String token;
}
