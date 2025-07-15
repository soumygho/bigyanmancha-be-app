package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import com.vigyanmancha.backend.domain.postgres.Student;
import com.vigyanmancha.backend.domain.postgres.StudentClass;
import com.vigyanmancha.backend.domain.postgres.VigyanKendraDetails;
import com.vigyanmancha.backend.dto.request.StudentRequestDTO;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.repository.postgres.SchoolDetailsRepository;
import com.vigyanmancha.backend.repository.postgres.StudentClassRepository;
import com.vigyanmancha.backend.repository.postgres.StudentRepository;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import com.vigyanmancha.backend.utility.mapper.StudentDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolDetailsRepository schoolDetailsRepository;
    private final StudentClassRepository studentClassRepository;
    private final VigyanKendraRepository vigyanKendraRepository;


    public List<StudentResponseDto> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(StudentDetailsMapper.studentDetailsMapper::mapFromEntity)
                .collect(Collectors.toList());
    }

    public StudentResponseDto getById(Long id) {
        return studentRepository.findById(id)
                .map(StudentDetailsMapper.studentDetailsMapper::mapFromEntity)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public StudentResponseDto create(StudentRequestDTO dto) {
        VigyanKendraDetails vigyanKendraDetails = vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .orElseThrow(() -> new RuntimeException("Vigyan kendra details not found"));
        SchoolDetails school =
                schoolDetailsRepository.findById(dto.getSchoolId())
                        .orElseThrow(() -> new RuntimeException("School details not found"));
        StudentClass studentClass = studentClassRepository.findById(dto.getStudentClassId())
                .orElseThrow(() -> new RuntimeException("Class details not found"));
        Student entity = new Student();
        entity.setName(dto.getName());
        entity.setSex(dto.getSex());
        entity.setSchoolDetails(school);
        entity.setStudentClass(studentClass);
        entity.setRoll(generateRoll(studentClass, vigyanKendraDetails));
        entity.setNumber(generateNumber(studentClass));
        entity.setVigyanKendraDetails(vigyanKendraDetails);
        return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(studentRepository.save(entity));
    }

    public StudentResponseDto update(StudentRequestDTO dto) {
        VigyanKendraDetails vigyanKendraDetails = vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .orElseThrow(() -> new RuntimeException("Vigyan kendra details not found"));
        SchoolDetails school =
                schoolDetailsRepository.findById(dto.getSchoolId())
                        .orElseThrow(() -> new RuntimeException("School details not found"));
        StudentClass studentClass = studentClassRepository.findById(dto.getStudentClassId())
                .orElseThrow(() -> new RuntimeException("Class details not found"));

        Student entity = studentRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        entity.setName(dto.getName());
        entity.setSex(dto.getSex());
        entity.setSchoolDetails(school);
        entity.setStudentClass(studentClass);
        if (Objects.nonNull(dto.getRollNumber())) {
            if (studentRepository.getCountByStudentRollAndNumber(entity.getRoll(), dto.getRollNumber().toString()) > 0
            && !Objects.equals(studentRepository.getByStudentRollAndNumber(entity.getRoll(), dto.getRollNumber().toString()).getId(), entity.getId())) {
                throw new RuntimeException("Roll number already exists");
            }
            entity.setNumber(dto.getRollNumber().toString());
        }
        entity.setVigyanKendraDetails(vigyanKendraDetails);
        return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(studentRepository.save(entity));
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    private String generateRoll(StudentClass studentClass, VigyanKendraDetails vigyanKendraDetails) {
        return vigyanKendraDetails.getCode() + "/" + studentClass.getName();
    }

    private String generateNumber(StudentClass studentClass) {
        int count = studentRepository.countAllByStudentClass(studentClass);
        ++count;
        return String.format("%04d", count);
    }
}

