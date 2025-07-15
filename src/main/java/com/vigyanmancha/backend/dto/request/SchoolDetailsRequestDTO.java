package com.vigyanmancha.backend.dto.request;

import lombok.Data;

@Data
public class SchoolDetailsRequestDTO {

    private Long id;
    private String name;
    private Long vigyanKendraId; // The ID of the associated VigyanKendraDetails
    private Long examCentreId; // The ID of the associated ExaminationCentreDetails
    private String vigyanKendraName;
    private String examCentreName;
}

