package com.dev.identityservice.Jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String signerKey;
    private long validDuration;
    private long refreshableDuration;
}
