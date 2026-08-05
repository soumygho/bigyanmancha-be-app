package com.vigyanmancha.backend.dto.reporting;

import lombok.Data;

@Data
public class StatisticsReportDto {
    private String vigyanKendraCode;
    private String className;
    private String examCenterName;
    private Long count;
}
