package com.dev.identityservice.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.dev.identityservice.dto.request.PermisionRequest;
import com.dev.identityservice.dto.response.PermisionResponse;
import com.dev.identityservice.entity.Permission;

@Component
@Mapper(componentModel = "spring") // khai bao cho srping  biet su dung no  là kiểu dependece ịnection
public interface PermisionMapper {
    Permission toPermission(PermisionRequest request);

    PermisionResponse toPermissionResponse(Permission request);
}
;
