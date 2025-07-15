package com.vigyanmancha.backend.domain.postgres;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
