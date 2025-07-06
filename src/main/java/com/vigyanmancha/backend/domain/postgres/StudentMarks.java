package com.vigyanmancha.backend.domain.postgres;
import com.vigyanmancha.backend.domain.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "student_marks")
public class StudentMarks extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Marks are required")
    @Min(value = 0, message = "Marks must be >= 0")
    private int marks;

    @NotNull(message = "Maximum marks are required")
    @Min(value = 1, message = "Maximum marks must be >= 1")
    private int maximumMarks;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id")
    private SubjectDetails subject;
}