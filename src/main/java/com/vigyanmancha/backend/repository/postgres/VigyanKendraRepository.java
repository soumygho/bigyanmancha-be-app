package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.VigyanKendraDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VigyanKendraRepository extends JpaRepository<VigyanKendraDetails, Long> {
    Optional<VigyanKendraDetails> findByCode(String code);
}