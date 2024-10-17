package com.dev.identityservice.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dev.identityservice.entity.Enum.Role;
import com.dev.identityservice.entity.User;
import com.dev.identityservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInnitConfig {

    private final PasswordEncoder passwordEncoder; // Ensure this is final and injected
    private static final String ADMIN_USER = "admin";
    String passWordAdmin = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER).isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.toString());
                User user = User.builder()
                        .username(ADMIN_USER)
                        .password(passwordEncoder.encode(passWordAdmin))
                        // .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been creased with default , please change it ");
            }
        };
    }
}
