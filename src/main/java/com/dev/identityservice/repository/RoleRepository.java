package com.dev.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dev.identityservice.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
