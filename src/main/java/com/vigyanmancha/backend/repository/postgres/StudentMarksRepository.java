package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.StudentMarks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {
}
