package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminOrVigyankendraUser;
import com.vigyanmancha.backend.annotation.AdminUser;
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
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
public class StudentClassController {
    private final StudentClassService studentClassService;

    @GetMapping(produces = "application/json")
    @AdminOrVigyankendraUser
    public List<StudentClassDetailsResponseDto> getAllClasses() {
        return studentClassService.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @AdminOrVigyankendraUser
    public StudentClassDetailsResponseDto getClassById(@PathVariable Long id) {
        return studentClassService.getById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public StudentClassDetailsResponseDto createClass(@RequestBody StudentClassRequestDTO requestDTO) {
        return studentClassService.create(requestDTO);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public StudentClassDetailsResponseDto updateClass(@RequestBody StudentClassRequestDTO requestDTO) {
        return studentClassService.update(requestDTO);
    }

    @DeleteMapping(path="/{id}")
    @AdminUser
    public void deleteClass(@PathVariable Long id) {
        studentClassService.delete(id);
    }
}
