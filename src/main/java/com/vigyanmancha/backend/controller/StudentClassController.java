package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.dto.request.StudentClassRequestDTO;
import com.vigyanmancha.backend.dto.response.StudentClassDetailsResponseDto;
import com.vigyanmancha.backend.service.StudentClassService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-class")
@Tag(name = "Student class api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StudentClassController {
    private final StudentClassService studentClassService;

    @GetMapping(produces = "application/json")
    public List<StudentClassDetailsResponseDto> getAllClasses() {
        return studentClassService.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public StudentClassDetailsResponseDto getClassById(@PathVariable Long id) {
        return studentClassService.getById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public StudentClassDetailsResponseDto createClass(@RequestBody StudentClassRequestDTO requestDTO) {
        return studentClassService.create(requestDTO);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public StudentClassDetailsResponseDto updateClass(@RequestBody StudentClassRequestDTO requestDTO) {
        return studentClassService.update(requestDTO);
    }

    @DeleteMapping(path="/{id}")
    public void deleteClass(@PathVariable Long id) {
        studentClassService.delete(id);
    }
}
