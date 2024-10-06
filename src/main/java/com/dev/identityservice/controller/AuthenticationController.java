package com.dev.identityservice.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.identityservice.dto.request.ApiResponse;
import com.dev.identityservice.dto.request.AuthenticationRequest;
import com.dev.identityservice.dto.response.AuthenticationResponse;
import com.dev.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
   
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request){
       AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
       return ApiResponse.<AuthenticationResponse>builder()
       .result(authenticationResponse)
       .build();
    }

    @PostMapping("/introspect")
    ApiResponse<AuthenticationResponse> introspect (@RequestBody AuthenticationRequest request) throws JOSEException, ParseException{
       AuthenticationResponse authenticationResponse = authenticationService.introspect(request);
       if(authenticationResponse.isIsvalid())
            authenticationResponse.setAuthenticated(true);
       return ApiResponse.<AuthenticationResponse>builder()
       .result(authenticationResponse)
       .build();
    }



}
