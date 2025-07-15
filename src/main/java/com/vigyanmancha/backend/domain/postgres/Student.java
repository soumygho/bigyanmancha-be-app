package com.vigyanmancha.backend.domain.postgres;

import com.vigyanmancha.backend.domain.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = false, nullable = false, name = "roll")
    private String roll;
    @Column(unique = false, nullable = false, name = "num")
    private String number;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolDetails schoolDetails;

    @ManyToOne
    @JoinColumn(name = "vigyan_kendra_id")
    private VigyanKendraDetails vigyanKendraDetails;

    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentClass studentClass;

    @ManyToOne
    @JoinColumn(name = "enrollment_session_id", nullable = false)
    private EnrollmentSession enrollmentSession;

    @NotNull
    @Pattern(regexp = "^(F|M|O)$", message = "Sex must be one of 'F', 'M', or 'O'")
    private String sex; // F, M, O
}

