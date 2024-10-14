package com.dev.identityservice.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dev.identityservice.dto.request.RoleRequest;
import com.dev.identityservice.dto.response.RoleResponse;
import com.dev.identityservice.entity.Permission;
import com.dev.identityservice.entity.Role;
import com.dev.identityservice.mapper.RoleMapper;
import com.dev.identityservice.repository.PermissionRepository;
import com.dev.identityservice.repository.RoleRepository;

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
public class RoleService {

    RoleMapper roleMapper;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.getPermissions()));
        role.setPermissions(permissions);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRole() {

        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
