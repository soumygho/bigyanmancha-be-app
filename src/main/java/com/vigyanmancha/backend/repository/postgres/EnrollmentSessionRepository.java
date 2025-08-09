package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.EnrollmentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentSessionRepository extends JpaRepository<EnrollmentSession, Long> {
    List<EnrollmentSession> findByActive(boolean active);
    List<EnrollmentSession> findByEnrollmentFreezed(boolean active);
    List<EnrollmentSession> findByModificationFreezed(boolean active);
}
