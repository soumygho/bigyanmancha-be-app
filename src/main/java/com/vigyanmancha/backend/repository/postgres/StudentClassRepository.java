package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
}
