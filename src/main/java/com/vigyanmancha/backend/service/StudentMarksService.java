package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.Student;
import com.vigyanmancha.backend.domain.postgres.StudentMarks;
import com.vigyanmancha.backend.domain.postgres.SubjectDetails;
import com.vigyanmancha.backend.dto.request.StudentMarksRequestDTO;
import com.vigyanmancha.backend.repository.postgres.StudentMarksRepository;
import com.vigyanmancha.backend.repository.postgres.StudentRepository;
import com.vigyanmancha.backend.repository.postgres.SubjectDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentMarksService {

    private final StudentMarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final SubjectDetailsRepository subjectRepository;

    public StudentMarksRequestDTO create(StudentMarksRequestDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        SubjectDetails subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        StudentMarks entity = new StudentMarks();
        entity.setMarks(dto.getMarks());
        entity.setMaximumMarks(dto.getMaximumMarks());
        entity.setStudent(student);
        entity.setSubject(subject);

        StudentMarks saved = marksRepository.save(entity);
        return toDto(saved);
    }

    public List<StudentMarksRequestDTO> getAll() {
        return marksRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public StudentMarksRequestDTO getById(Long id) {
        return marksRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("StudentMarks not found"));
    }

    public void deleteById(Long id) {
        marksRepository.deleteById(id);
    }

    private StudentMarksRequestDTO toDto(StudentMarks marks) {
        StudentMarksRequestDTO dto = new StudentMarksRequestDTO();
        dto.setId(marks.getId());
        dto.setMarks(marks.getMarks());
        dto.setMaximumMarks(marks.getMaximumMarks());
        dto.setStudentId(marks.getStudent().getId());
        dto.setSubjectId(marks.getSubject().getId());
        return dto;
    }
}

