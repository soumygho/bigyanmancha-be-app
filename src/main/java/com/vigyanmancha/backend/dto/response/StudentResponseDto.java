package com.vigyanmancha.backend.dto.response;
import lombok.Data;

@Data
public class StudentResponseDto {
    private Long id;
    private String name;
    private String rollNumber;
    private Long schoolId;
    private String schoolName;
    private Long vigyanKendraId;
    private String vigyanKendraName;
    private Long classId;
    private String className;
    private String sex;
    //optional
    private Long examinationCentreId;
    private String examinationCentreName;
}
