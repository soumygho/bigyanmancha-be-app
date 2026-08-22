package com.vigyanmancha.backend.repository.postgres;


import com.vigyanmancha.backend.domain.postgres.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    int countAllByStudentClass(StudentClass studentClass);
    @Query("SELECT count (s) FROM Student s where s.studentClass = :class AND s.number = :number")
    int getCountByStudentClassAndNumber(@Param("class") StudentClass studentClass, @Param("number") String studentNumber);
    @Query("SELECT s FROM Student s where s.roll = :roll AND s.number = :number")
    Student getByStudentRollAndNumber(@Param("roll") String studentRoll, @Param("number") String studentNumber);
    @Query("SELECT s FROM Student s where s.vigyanKendraDetails.id = :vigyanKendraId")
    List<Student> findByVigyanKendra(@Param("vigyanKendraId") long id);

    @Query("SELECT s FROM Student s where s.studentClass = :class AND s.enrollmentSession = :enrollmentSession")
    List<Student> findByClassAndEnrollmentSession(@Param("class") StudentClass studentClass,
                                        @Param("enrollmentSession")EnrollmentSession enrollmentSession);

    @Query("SELECT s FROM Student s where s.vigyanKendraDetails.id = :vigyanKendraId AND s.enrollmentSession = :enrollmentSession")
    List<Student> findByVigyanKendraAndEnrollmentSession(@Param("vigyanKendraId") Long vigyanKendraId,
                                                          @Param("enrollmentSession")EnrollmentSession enrollmentSession);
    @Query("SELECT s FROM Student s where s.vigyanKendraDetails.id = :vigyanKendraId AND s.enrollmentSession = :enrollmentSession AND s.studentClass = :class")
    List<Student> findByVigyanKendraAndEnrollmentSessionAndClass(@Param("vigyanKendraId") Long vigyanKendraId,
                                                         @Param("enrollmentSession")EnrollmentSession enrollmentSession,
                                                         @Param("class") StudentClass studentClass);

    @Query("SELECT s FROM Student s where s.vigyanKendraDetails.id = :vigyanKendraId AND s.enrollmentSession = :enrollmentSession AND s.schoolDetails.examinationCentre = :examcenter")
    List<Student> findByVigyanKendraAndEnrollmentSessionAndExamCenter(@Param("vigyanKendraId") Long vigyanKendraId,
                                                                 @Param("enrollmentSession")EnrollmentSession enrollmentSession,
                                                                 @Param("examcenter")ExaminationCentreDetails examinationCentreDetails);
    @Query("SELECT s FROM Student s where s.vigyanKendraDetails.id = :vigyanKendraId AND s.enrollmentSession = :enrollmentSession AND s.schoolDetails.examinationCentre = :examcenter AND s.studentClass = :class")
    List<Student> findByVigyanKendraAndEnrollmentSessionAndExamCenterAndClass(@Param("vigyanKendraId") Long vigyanKendraId,
                                                                      @Param("enrollmentSession")EnrollmentSession enrollmentSession,
                                                                      @Param("examcenter")ExaminationCentreDetails examinationCentreDetails,
                                                                      @Param("class") StudentClass studentClass);
    @Query("SELECT count (s) FROM Student s where s.studentClass = :class AND s.schoolDetails.examinationCentre = :examcenter")
    long getCountByStudentClassAndExamCenter(@Param("class") StudentClass studentClass,
                                             @Param("examcenter")ExaminationCentreDetails examinationCentreDetails);
}
