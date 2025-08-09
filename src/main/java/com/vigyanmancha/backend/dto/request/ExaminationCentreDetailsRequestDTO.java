package com.vigyanmancha.backend.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ExaminationCentreDetailsRequestDTO {

    private Long id;
    private String name;
    private Long schoolDetailsId; // The ID of the associated SchoolDetails entity
    private Long vigyanKendraId;
    private Set<String> schoolNames;
    private String vigyanKendraName;
    private String vigyanKendraCode;
    private String schoolName;
    private Long enrollmentId;
    private int enrollmentYear;
}


