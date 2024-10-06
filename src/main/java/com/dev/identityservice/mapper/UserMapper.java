package com.dev.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import com.dev.identityservice.dto.request.UserCreationRequest;
import com.dev.identityservice.dto.request.UserUpdateRequest;
import com.dev.identityservice.dto.response.UserResponse;
import com.dev.identityservice.entity.User;

import java.util.List;

@Component
@Mapper(componentModel = "spring")// khai bao cho srping  biet su dung no  là kiểu dependece ịnection
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "lastName", source = "lastName")
    UserResponse toUserResponse(User request);
     
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    public List<UserResponse> toUserResponse(List<User> users) ;
}
