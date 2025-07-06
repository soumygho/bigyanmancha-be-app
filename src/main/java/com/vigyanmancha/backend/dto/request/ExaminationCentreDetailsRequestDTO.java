package com.vigyanmancha.backend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ExaminationCentreDetailsRequestDTO {

    private Long id;
    private String name;
    private Long schoolDetailsId; // The ID of the associated SchoolDetails entity
    private Long vigyanKendraId;
}


