package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentSessionRepository extends JpaRepository<Student, Long> {
}
