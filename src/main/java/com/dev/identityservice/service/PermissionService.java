package com.dev.identityservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.identityservice.dto.request.PermisionRequest;
import com.dev.identityservice.dto.response.PermisionResponse;
import com.dev.identityservice.entity.Permission;
import com.dev.identityservice.mapper.PermisionMapper;
import com.dev.identityservice.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(
        level = AccessLevel.PRIVATE,
        makeFinal = true) // tao cac truong  kieu  private final UserRepository userRepository;
@Slf4j
public class PermissionService {

    PermisionMapper permisionMapper;
    PermissionRepository permissionRepository;

    public PermisionResponse createPermision(PermisionRequest request) {

        Permission permission = permisionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permisionMapper.toPermissionResponse(permission);
    }

    public List<PermisionResponse> getAllPermision() {
        // List<PermisionResponse> permissions = new ArrayList<>();
        // permissionRepository.findAll().forEach(permission -> permisionMapper.toPermissionResponse(permission));

        return permissionRepository.findAll().stream()
                .map(permission -> permisionMapper.toPermissionResponse(permission))
                .collect(Collectors.toList());
    }

    public void deletePermisison(String permission) {
        permissionRepository.deleteById(permission);
    }
}
