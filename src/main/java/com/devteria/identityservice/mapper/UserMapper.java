package com.devteria.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import java.util.List;
import com.devteria.identityservice.entity.User;
import com.devteria.identityservice.dto.request.UserCreationRequest;
import com.devteria.identityservice.dto.request.UserUpdateRequest;
import com.devteria.identityservice.dto.response.UserResponse;

@Component
@Mapper(componentModel = "spring")// khai bao cho srping  biet su dung no  là kiểu dependece ịnection
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "lastName", source = "lastName")
    UserResponse toUserResponse(User request);
     
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    public List<UserResponse> toUserResponse(List<User> users) ;
}
