package com.dev.identityservice.configuration;

import org.hibernate.grammars.hql.HqlParser.RootEntityContext;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dev.identityservice.entity.User;
import com.dev.identityservice.entity.Enum.Role;
import com.dev.identityservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInnitConfig {

    private final PasswordEncoder passwordEncoder; // Ensure this is final and injected

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name().toString());
                User user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                // .roles(roles) 
                .build();

                userRepository.save(user);
                log.warn("admin user has been creased with default , please change it ");
            }
        };
    }
}
