package com.vigyanmancha.backend.dto.request;

import lombok.Data;

@Data
public class StudentRequestDTO {
    private Long id;
    private String name;
    private String sex;
    private String rollNumber;
    private Long schoolId; // The ID of the associated SchoolDetails
    private Long vigyanKendraId; // The ID of the associated VigyanKendraDetails
    private Long studentClassId;
}


