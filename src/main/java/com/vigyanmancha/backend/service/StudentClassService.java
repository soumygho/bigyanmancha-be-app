package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.StudentClass;
import com.vigyanmancha.backend.domain.postgres.SubjectDetails;
import com.vigyanmancha.backend.dto.request.StudentClassRequestDTO;
import com.vigyanmancha.backend.dto.response.StudentClassDetailsResponseDto;
import com.vigyanmancha.backend.repository.postgres.StudentClassRepository;
import com.vigyanmancha.backend.repository.postgres.SubjectDetailsRepository;
import com.vigyanmancha.backend.utility.mapper.StudentClassMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentClassService {
    private final StudentClassRepository studentClassRepository;
    private final SubjectDetailsRepository subjectDetailsRepository;

    public StudentClassDetailsResponseDto toDto(StudentClass studentClass) {
        return StudentClassMapper.INSTANCE.mapFromEntity(studentClass);
    }

    public List<StudentClassDetailsResponseDto> getAll() {
        return studentClassRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public StudentClassDetailsResponseDto getById(Long id) {
        StudentClass studentClass = studentClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student class not found"));
        return toDto(studentClass);
    }

    public void delete(Long id) {
        StudentClass studentClass = studentClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student class not found"));
        studentClassRepository.delete(studentClass);
    }

    public StudentClassDetailsResponseDto create(StudentClassRequestDTO dto) {
        StudentClass studentClass = new StudentClass();
        studentClass.setName(dto.getName());
        return toDto(studentClassRepository.save(studentClass));
    }

    public StudentClassDetailsResponseDto update(StudentClassRequestDTO dto) {
        log.info(dto.toString());
        if (CollectionUtils.isEmpty(dto.getSubjectIds())) {
            dto.setSubjectIds(Collections.emptyList());
        }
        StudentClass studentClass = studentClassRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Student class not found"));
        Set<SubjectDetails> subjectDetailsList = new HashSet<>(subjectDetailsRepository.findAllById(dto.getSubjectIds()));
        studentClass.setName(dto.getName());
        studentClass.setSubjects(subjectDetailsList);
        return toDto(studentClassRepository.save(studentClass));
    }
}
