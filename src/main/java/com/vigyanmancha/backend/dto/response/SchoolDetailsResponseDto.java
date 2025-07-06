package com.vigyanmancha.backend.dto.response;
import lombok.Data;

@Data
public class SchoolDetailsResponseDto {
    private Long id;
    private String name;
    private Long vigyanKendraId;
    private String vigyanKendraName;
    private Long examinationCentreId;
    private String examinationCentreName;
}
