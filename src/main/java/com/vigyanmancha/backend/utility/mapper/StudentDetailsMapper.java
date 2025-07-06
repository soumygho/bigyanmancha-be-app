package com.vigyanmancha.backend.utility.mapper;

import com.vigyanmancha.backend.domain.postgres.Student;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentDetailsMapper {
    StudentDetailsMapper studentDetailsMapper = Mappers.getMapper(StudentDetailsMapper.class);

    @Mapping(target = "schoolId", expression = "java(student.getSchoolDetails().getId())")
    @Mapping(target = "schoolName", expression = "java(student.getSchoolDetails().getName())")
    @Mapping(target = "vigyanKendraId", expression = "java(student.getVigyanKendraDetails().getId())")
    @Mapping(target = "vigyanKendraName", expression = "java(student.getVigyanKendraDetails().getName())")
    @Mapping(target = "classId", expression = "java(student.getStudentClass().getId())")
    @Mapping(target = "className", expression = "java(student.getStudentClass().getName())")
    @Mapping(target = "examinationCentreId",
            expression = "java(student.getSchoolDetails().getExaminationCentre() != null ? student.getSchoolDetails().getExaminationCentre().getId() : 0)")

    @Mapping(target = "examinationCentreName",
            expression = "java(student.getSchoolDetails().getExaminationCentre() != null ? student.getSchoolDetails().getExaminationCentre().getName() : \"\")")
    StudentResponseDto mapFromEntity(Student student);
}
