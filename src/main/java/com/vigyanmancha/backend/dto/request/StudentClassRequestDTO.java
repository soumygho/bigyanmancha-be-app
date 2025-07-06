package com.vigyanmancha.backend.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class StudentClassRequestDTO {
    private Long id;
    private String name;
    private List<Long> subjectIds; // List of Subject IDs
}

