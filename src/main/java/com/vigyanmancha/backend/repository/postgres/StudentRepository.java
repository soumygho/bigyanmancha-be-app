package com.vigyanmancha.backend.repository.postgres;


import com.vigyanmancha.backend.domain.postgres.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
