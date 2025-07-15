package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUserName(String username);

    Boolean existsByUserName(String username);
}
