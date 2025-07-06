package com.vigyanmancha.backend.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class StudentClassDetailsResponseDto {
    private Long id;
    private String name;
    private Set<SubjectDetailsResponseDto> subjects;
}
