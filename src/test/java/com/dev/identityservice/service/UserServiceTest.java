package com.dev.identityservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.dev.identityservice.dto.request.UserCreationRequest;
import com.dev.identityservice.dto.response.UserResponse;
import com.dev.identityservice.entity.User;
import com.dev.identityservice.exception.AppException;
import com.dev.identityservice.repository.RoleRepository;
import com.dev.identityservice.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;
    private String userId;

    @BeforeEach
    public void initData() {
        dob = LocalDate.of(1997, 1, 1);
        request = UserCreationRequest.builder()
                .username("phong2")
                .firstName("nguyen")
                .lastName("duy")
                .password("12345")
                .roles(List.of("Admin"))
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .username("phong2")
                .firstName("nguyen")
                .lastName("duy")
                .dob(dob)
                .build();
        user = User.builder()
                .id("ed38ca0cd7aa")
                .username("phong2")
                .firstName("nguyen")
                .lastName("duy")
                .dob(dob)
                .build();

        userId = "ed38ca0cd7aa";
    }

    @Test
    void createUserSuccessTest() {
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(roleRepository.findAllById(request.getRoles())).thenReturn(new ArrayList<>());

        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponse response = userService.createUser(request);

        assertEquals("phong2", response.getUsername());
        assertEquals("nguyen", response.getFirstName());
    }

    @Test
    void createUserFailTest() {

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);
        // when
        var userExistException = assertThrows(AppException.class, () -> userService.createUser(request));

        assertEquals(userExistException.getErrorCode().getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(userExistException.getErrorCode().getCode(), 1002);
    }

    @Test
    void deleteUserSuccessTest() {
        // when
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @WithMockUser(username = "phong2")
    void getMyInfoSuccessTest() {
        // give
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        //        when( SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("phong2");
        // when
        UserResponse response = userService.getInfo();
        // then
        assertNotNull(response);
        assertEquals("phong2", response.getUsername());
        assertEquals("nguyen", response.getFirstName());
        assertEquals("duy", response.getLastName());
        assertEquals(dob, response.getDob());
    }

    @Test
    @WithMockUser(username = "phong2")
    void getMyInfoFailsTest() {
        // give
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        // when
        var userException = assertThrows(AppException.class, () -> userService.getInfo());
        // then

        assertEquals(userException.getErrorCode().getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(userException.getErrorCode().getCode(), 1005);
    }
}
