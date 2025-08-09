package com.vigyanmancha.backend.domain.postgres;

import com.vigyanmancha.backend.domain.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "vigyan_kendra_details")
public class VigyanKendraDetails extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;

    @OneToMany(mappedBy = "vigyanKendraDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SchoolDetails> schoolDetails;

    @OneToMany(mappedBy = "vigyanKendraDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students;

    @OneToMany(mappedBy = "vigyanKendraDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ExaminationCentreDetails> examinationCentres;

    @OneToMany(mappedBy = "vigyanKendraDetails", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserDetails> users;
}

