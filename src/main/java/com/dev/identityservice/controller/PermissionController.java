package com.dev.identityservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.identityservice.dto.request.ApiResponse;
import com.dev.identityservice.dto.request.PermisionRequest;
import com.dev.identityservice.dto.response.PermisionResponse;
import com.dev.identityservice.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping("/create")
    public ApiResponse<PermisionResponse> createPermisson(@RequestBody PermisionRequest request) {
        return ApiResponse.<PermisionResponse>builder()
                .result(permissionService.createPermision(request))
                .build();
    }

    @GetMapping("/get-all")
    public ApiResponse<List<PermisionResponse>> getAll() {
        return ApiResponse.<List<PermisionResponse>>builder()
                .result(permissionService.getAllPermision())
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> deletePermisson(@PathVariable String permission) {
        permissionService.deletePermisison(permission);
        return ApiResponse.<Void>builder().build();
    }
}
