package com.vigyanmancha.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DRSheetResponse {
    private String vigyanKendraName;
    private String vigyanKendraCode;
    private String examCenterName;
    private String className;
    private List<StudentDetails> students;
}
