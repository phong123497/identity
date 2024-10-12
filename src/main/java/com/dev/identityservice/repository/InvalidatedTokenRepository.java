package com.dev.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.identityservice.entity.InvalidatedToken;


public interface InvalidatedTokenRepository  extends JpaRepository<InvalidatedToken, String> {

    
} 
