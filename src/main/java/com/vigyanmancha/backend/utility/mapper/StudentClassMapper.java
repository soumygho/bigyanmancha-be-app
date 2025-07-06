package com.vigyanmancha.backend.utility.mapper;

import com.vigyanmancha.backend.domain.postgres.StudentClass;
import com.vigyanmancha.backend.dto.response.StudentClassDetailsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentClassMapper {
    StudentClassMapper INSTANCE = Mappers.getMapper(StudentClassMapper.class);

    StudentClassDetailsResponseDto mapFromEntity(StudentClass studentClass);
}
