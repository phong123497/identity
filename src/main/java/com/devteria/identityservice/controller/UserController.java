package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.ApiResponse;
import com.devteria.identityservice.dto.request.UserCreationRequest;
import com.devteria.identityservice.dto.request.UserUpdateRequest;
import com.devteria.identityservice.dto.response.UserResponse;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;

// import jakarta.validation.Valid;


import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    UserService userService;


    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody  UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    public  ApiResponse<List<UserResponse>>  getUsers(){
        return  ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    
    @GetMapping("/myinfor")
    public ApiResponse<UserResponse> getInfor(){
        return  ApiResponse.<UserResponse>builder()
        .result(userService.getInfor())
        .build();
    }


    @GetMapping("/{userId}")
    ApiResponse <UserResponse> getUser(@PathVariable("userId") String userId){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("userName: {}", authentication.getName());
        authentication.getAuthorities().forEach(authority -> log.info("authority: {}", authority.getAuthority()));
        

        return ApiResponse.<UserResponse>builder()
        .result(userService.getUser(userId))
        .build();
    }


    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return  ApiResponse.<UserResponse>builder()
        .result (userService.updateUser(userId, request))
        .build();
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return "User has been deleted";
    }
    
}
