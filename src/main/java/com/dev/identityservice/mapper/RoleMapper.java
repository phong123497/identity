package com.dev.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.dev.identityservice.dto.request.RoleRequest;
import com.dev.identityservice.dto.response.RoleResponse;
import com.dev.identityservice.entity.Role;

@Component
@Mapper(componentModel = "spring") // khai bao cho srping  biet su dung no  là kiểu dependece ịnection
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
;
