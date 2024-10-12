package com.dev.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;
import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dev.identityservice.Jwt.JwtProperties;
import com.dev.identityservice.dto.request.AuthenticationRequest;
import com.dev.identityservice.dto.request.LogoutRequest;
import com.dev.identityservice.dto.response.AuthenticationResponse;
import com.dev.identityservice.entity.InvalidatedToken;
import com.dev.identityservice.entity.User;
import com.dev.identityservice.exception.AppException;
import com.dev.identityservice.exception.ErrorCode;
import com.dev.identityservice.repository.InvalidatedTokenRepository;
import com.dev.identityservice.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// @ConfigurationProperties(prefix = "jwt")
public class AuthenticationService {

    // @Value("${jwt.signerKey}")
    // private String signerKey;
    JwtProperties jwtProperties;

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticate)
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return AuthenticationResponse.builder()
                .token(generateToken(user))
                .authenticated(authenticate)
                .build();

    }

    public AuthenticationResponse introspect(AuthenticationRequest request) throws JOSEException, ParseException {
        boolean isValid = true;
        try {
            verifyToken(request.getToken());
        } catch (AppException e) {
           isValid = false;
        }
        
        return AuthenticationResponse.builder()
        .isvalid(isValid)
        .build();
    }

    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtProperties.getSignerKey().getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
    
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
    
        if (!(verified && expiryTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        if (invalidatedTokenRepository.existsById(jwtId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    
        return signedJWT;
    }

    public void logout(LogoutRequest request) throws JOSEException, ParseException {
        SignedJWT verifiedToken = verifyToken(request.getToken());
    
        String jwtId = verifiedToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = verifiedToken.getJWTClaimsSet().getExpirationTime();
    
        if (!invalidatedTokenRepository.existsById(jwtId)) {
            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(jwtId)
                    .expiryTime(expirationTime)
                    .build());
        }
    }




    String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("phong")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 6000000))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(jwtProperties.getSignerKey().getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {

            throw new RuntimeException(e);
        }

    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

};