package com.vigyanmancha.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentMarksRequestDTO {
    private Long id;

    @NotNull(message = "Marks are required")
    @Min(value = 0, message = "Marks must be >= 0")
    private Integer marks;

    @NotNull(message = "Maximum marks are required")
    @Min(value = 1, message = "Maximum marks must be >= 1")
    private Integer maximumMarks;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;
}

