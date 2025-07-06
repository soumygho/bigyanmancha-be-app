package com.vigyanmancha.backend.domain.postgres;

import com.vigyanmancha.backend.domain.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "subject_details")
public class SubjectDetails extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private Set<StudentClass> studentClasses;
}
