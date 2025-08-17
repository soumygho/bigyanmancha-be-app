package com.vigyanmancha.backend.domain.postgres;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class TaskDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "enrollment_year", nullable = false)
    private int enrollmentYear;
    @Column(name = "vigyankendra_code", nullable = true)
    private String vigyanKendraCode;
    @Column(name = "vigyankendra_name", nullable = true)
    private String VigyanKendraName;
    @Column(name = "current_date", nullable = false)
    private String date;
}
