package com.vigyanmancha.backend.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class SubjectAssignDeAssignRequest {
    private Set<Long> subjectIds;
    private Long studentClassId;
}
