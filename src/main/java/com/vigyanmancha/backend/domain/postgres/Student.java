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

    @Column(unique = true, nullable = false, name = "roll_number")
    private String rollNumber;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolDetails schoolDetails;

    @ManyToOne
    @JoinColumn(name = "vigyan_kendra_id")
    private VigyanKendraDetails vigyanKendraDetails;

    @ManyToOne
    @JoinColumn(name = "student_class_id")
    private StudentClass studentClass;

    @NotNull
    @Pattern(regexp = "^(F|M|O)$", message = "Sex must be one of 'F', 'M', or 'O'")
    private String sex; // F, M, O
}

