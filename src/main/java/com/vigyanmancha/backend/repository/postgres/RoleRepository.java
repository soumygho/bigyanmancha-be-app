package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.Role;
import com.vigyanmancha.backend.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}