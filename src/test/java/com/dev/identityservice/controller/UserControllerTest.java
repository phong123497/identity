package com.dev.identityservice.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.dev.identityservice.dto.request.UserCreationRequest;
import com.dev.identityservice.dto.response.UserResponse;
import com.dev.identityservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserResponse userResponse;
    private UserCreationRequest userCreationRequest;
    private LocalDate dob;

    @BeforeEach
    public void initData() {
        dob = LocalDate.of(1997, 1, 1);
        userCreationRequest = UserCreationRequest.builder()
                .username("phong")
                .firstName("nguyen")
                .lastName("duy")
                .password("12345")
                .dob(dob)
                .build();
        userResponse = UserResponse.builder()
                .id("ed38ca0cd7aa")
                .username("phong")
                .firstName("nguyen")
                .lastName("duy")
                .dob(dob)
                .build();
    }

    @Test
    void createUserTest() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        Mockito.when(userService.createUser(userCreationRequest)).thenReturn(userResponse);
        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("ed38ca0cd7aa"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("phong"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("nguyen"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("duy"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.dob").value(dob.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
    }

    @Test
    void InvalPaswordTets() throws Exception {

        // given
        userCreationRequest.setPassword("12");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        Mockito.when(userService.createUser(userCreationRequest)).thenReturn(userResponse);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password must be at least 3 characters"))
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1004));
    }
}
