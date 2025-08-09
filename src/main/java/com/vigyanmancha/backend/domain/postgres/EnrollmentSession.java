package com.vigyanmancha.backend.domain.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enrollment_session")
public class EnrollmentSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "year", nullable = false)
    private Integer year;
    @Column(name = "active")
    private boolean active;
    @Column(name = "enrollment_freezed")
    private  boolean enrollmentFreezed;
    @Column(name = "modification_freezed")
    private boolean modificationFreezed;

    @OneToMany(mappedBy = "enrollmentSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SchoolDetails> schoolDetails;

    @OneToMany(mappedBy = "enrollmentSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Student> students;

    @OneToMany(mappedBy = "enrollmentSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ExaminationCentreDetails> examinationCentres;
    @OneToMany(mappedBy = "enrollmentSession", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SubjectDetails> subjects;
}
