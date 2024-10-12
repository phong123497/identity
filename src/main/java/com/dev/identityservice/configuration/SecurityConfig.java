package com.dev.identityservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import java.text.ParseException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.dev.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;


import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  /// phan quyen tren menthold
// khi lop cau hinh la configuration thi no se dc init len va tiem nhg bean cua no vao application context
public class SecurityConfig {
    
    @Value("${jwt.signerKey}")
    private String signerKey;
    private AuthenticationService authenticationService;

    public  SecurityConfig (AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    private final String [] PUBLIC_ENDPOINTS = {"/users", "/auth/login","/auth/introspect","/auth/login"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests(request -> 
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()// thuc hien bat dau xet nhg end point nao co quyen truc tiep truy cap
                    // .requestMatchers(HttpMethod.GET ,"/users").hasAnyAuthority("ROLE_ADMIN") // cấu hình thêm chỉ admin mới có quyền truy cập 
                    // .requestMatchers(HttpMethod.GET, "/users/*").hasRole(Role.ADMIN.name())
                    .anyRequest().authenticated());
                    

        httpSecurity.oauth2ResourceServer( (oauth2)->
        oauth2.jwt((jwtConfigurer) -> jwtConfigurer.decoder(jwtDecoder()) 
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                    );
        
        return httpSecurity.build();
    }

    /*
        author provider   no dung decoder co hop le khong
        chứa nằng của nó kiểm tra singerKey hợp lệ hay k
        token có hết hạn hay k, kiểm tra scope, roles , xem có đúng k , cau hinh mac dinh
    */
    @Bean
    JwtDecoder jwtDecoder(){ 
        SecretKeySpec  secretKeySpec =  new SecretKeySpec(signerKey.getBytes(), "HS512");

        JwtDecoder jwtDecoder =   NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
        //　custom decoder
        return  new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                try {
                    authenticationService.verifyToken(token);
                    Jwt jwt = jwtDecoder.decode(token);
                    return jwt;
                } catch (JOSEException | ParseException e) {
                     throw new JwtException(e.getMessage());
                }
            }
        };
    }

    /*
     * converter 
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
