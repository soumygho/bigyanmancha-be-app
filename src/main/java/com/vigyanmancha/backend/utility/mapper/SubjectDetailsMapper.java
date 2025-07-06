package com.vigyanmancha.backend.utility.mapper;

import com.vigyanmancha.backend.domain.postgres.SubjectDetails;
import com.vigyanmancha.backend.dto.response.SubjectDetailsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectDetailsMapper {
    SubjectDetailsMapper INSTANCE = Mappers.getMapper(SubjectDetailsMapper.class);

    SubjectDetailsResponseDto mapFromEntity(SubjectDetails entity);
}
