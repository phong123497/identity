package com.devteria.identityservice.service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.ParseException;
import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.devteria.identityservice.repository.UserRepository;
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
import com.devteria.identityservice.Jwt.JwtProperties;
import com.devteria.identityservice.dto.request.AuthenticationRequest;
import com.devteria.identityservice.dto.response.AuthenticationResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.exception.ErrorCode;
import com.devteria.identityservice.exception.AppException;

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

   
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var  user = userRepository.findByUsername(request.getUsername())
        .orElseThrow( () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate =  passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticate)
            throw new AppException(ErrorCode.UNAUTHORIZED);
        
        return AuthenticationResponse.builder()
        .token(generateToken(user))
        .authenticated(authenticate)
        .build();
        
    }
    public AuthenticationResponse introspect(AuthenticationRequest request) throws JOSEException, ParseException {
        String  token = request.getToken();
        
        JWSVerifier verifier = new MACVerifier(jwtProperties.getSignerKey().getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var  verified = signedJWT.verify(verifier);
        return AuthenticationResponse.builder()
        .isvalid(verified && expiryTime.after(new Date()))
        .build();
    }

 
    

    String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer("phong")
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + 600000)) ///Fixed expiration time calculation
            .claim("scope", buildScope(user)) 
            .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload );
        

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
            user.getRoles().forEach(role -> stringJoiner.add(role));

        return stringJoiner.toString();
    }
}