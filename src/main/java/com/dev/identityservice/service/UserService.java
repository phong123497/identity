package com.dev.identityservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.identityservice.dto.request.UserCreationRequest;
import com.dev.identityservice.dto.request.UserUpdateRequest;
import com.dev.identityservice.dto.response.UserResponse;
import com.dev.identityservice.entity.User;
import com.dev.identityservice.entity.Role;
import com.dev.identityservice.exception.AppException;
import com.dev.identityservice.exception.ErrorCode;
import com.dev.identityservice.mapper.UserMapper;
import com.dev.identityservice.repository.RoleRepository;
import com.dev.identityservice.repository.UserRepository;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // tao cac truong  kieu  private final UserRepository userRepository;
@Slf4j
public class UserService {
    UserRepository userRepository;

    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    PermissionService permissionService;
    RoleRepository roleRepository;
    

    public UserResponse createUser(UserCreationRequest request){

        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        
        User user = userMapper.toUser(request);

        
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles =  new HashSet<>(roleRepository.findAllById(request.getRoles()));
        // roles.add(Role.USER.name());
        user.setRoles(roles);
        userRepository.save(user);
        return userMapper.toUserResponse( userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role>  roles = new HashSet<>(roleRepository.findAllById(request.getRoles()));
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")// kiem tra trc roi moi vao ham xac nhan
    // @PreAuthorize("hasAuthority('create_data')")// kiem tra trc roi moi vao ham xac nhan
    // @PreAuthorize("principal?.isAdmin()")
    public List<UserResponse> getUsers(){
        
        return userMapper.toUserResponse(userRepository.findAll());
    }
    

    // @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #username == authentication.name)")
	@PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')") //  vao ham roi xong moi kiem tra
    public UserResponse getUser(String id){
        // var  authenticate = Authencon
        log.info("in  getUser");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new  AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public  UserResponse  getInfor(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User byUserName = userRepository.findByUsername(name).orElseThrow( () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        
        return userMapper.toUserResponse(byUserName);
    }

}
