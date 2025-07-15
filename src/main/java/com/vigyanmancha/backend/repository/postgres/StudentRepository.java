package com.vigyanmancha.backend.repository.postgres;


import com.vigyanmancha.backend.domain.postgres.Student;
import com.vigyanmancha.backend.domain.postgres.StudentClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    int countAllByStudentClass(StudentClass studentClass);
    @Query("SELECT count (s) FROM Student s where s.roll = :roll AND s.number = :number")
    int getCountByStudentRollAndNumber(@Param("roll") String studentRoll, @Param("number") String studentNumber);
    @Query("SELECT s FROM Student s where s.roll = :roll AND s.number = :number")
    Student getByStudentRollAndNumber(@Param("roll") String studentRoll, @Param("number") String studentNumber);
}
