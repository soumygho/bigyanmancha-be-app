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
@RequiredArgsConstructor
public class StudentClassController {
    private final StudentClassService studentClassService;

    @GetMapping
    public List<StudentClassDetailsResponseDto> getAll() {
        return studentClassService.getAll();
    }

    @GetMapping("/{id}")
    public StudentClassDetailsResponseDto getById(@PathVariable Long id) {
        return studentClassService.getById(id);
    }

    @PostMapping
    public StudentClassDetailsResponseDto create(@RequestBody StudentClassRequestDTO requestDTO) {
        return studentClassService.create(requestDTO);
    }

    @PutMapping
    public StudentClassDetailsResponseDto update(@RequestBody StudentClassRequestDTO requestDTO) {
        return studentClassService.update(requestDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentClassService.delete(id);
    }
}
