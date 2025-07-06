package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.StudentRequestDTO;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Student enrollment api")
@RequiredArgsConstructor
public class StudentController {


    private final StudentService studentService;

    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PostMapping
    public StudentResponseDto createStudent(@RequestBody StudentRequestDTO studentDTO) {
        return studentService.create(studentDTO);
    }
}

