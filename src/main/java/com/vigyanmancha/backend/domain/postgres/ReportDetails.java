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
@Table(name = "reporting_details")
public class ReportDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "report_key", nullable = false)
    private String reportKey;
    @Column(name = "enrollment_year", nullable = false)
    private int enrollmentYear;
    @Column(name = "vigyankendra_code", nullable = false)
    private String vigyanKendraCode = "GLOBAL";
    @Column(name = "vigyankendra_name", nullable = false)
    private String VigyanKendraName = "GLOBAL";
    @Column(name = "report_date", nullable = false)
    private String reportDate;
    @Column(name = "report_name")
    private String reportName;
    @Column(name = "status", nullable = false)
    private String status;
}
