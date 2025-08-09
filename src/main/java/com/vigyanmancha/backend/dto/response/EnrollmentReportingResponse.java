package com.vigyanmancha.backend.dto.response;

import lombok.Data;

@Data
public class EnrollmentReportingResponse {
    private Long id;
    private int enrollmentYear;
    private String reportKey;
    private String vigyanKendraCode;
    private String VigyanKendraName;
    private String reportDate;
    private String status;
}
