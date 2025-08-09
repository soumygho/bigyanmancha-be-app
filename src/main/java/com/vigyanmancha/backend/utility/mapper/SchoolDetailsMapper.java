package com.vigyanmancha.backend.utility.mapper;

import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import com.vigyanmancha.backend.dto.response.SchoolDetailsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SchoolDetailsMapper {
    SchoolDetailsMapper INSTANCE = Mappers.getMapper(SchoolDetailsMapper.class);
    @Mapping(target = "vigyanKendraId", expression = "java(schoolDetails.getVigyanKendraDetails().getId())")
    @Mapping(target = "vigyanKendraName", expression = "java(schoolDetails.getVigyanKendraDetails().getName())")
    @Mapping(target = "examCentreId", expression = "java(schoolDetails.getExaminationCentre() != null ? schoolDetails.getExaminationCentre().getId(): 0)")
    @Mapping(target = "examCentreName", expression = "java(schoolDetails.getExaminationCentre() != null ? schoolDetails.getExaminationCentre().getName(): \"\")")
    SchoolDetailsResponseDto mapFromEntity(SchoolDetails schoolDetails);
}
