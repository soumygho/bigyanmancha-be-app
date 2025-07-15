package com.vigyanmancha.backend.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class SchoolAssignDeAssignRequest {
    private Set<Long> schoolIds;
    private Long examinationCentreId;
}
