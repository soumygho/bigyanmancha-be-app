package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.StudentClassRequestDTO;
import com.vigyanmancha.backend.dto.request.SubjectDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.StudentClassDetailsResponseDto;
import com.vigyanmancha.backend.dto.response.SubjectDetailsResponseDto;
import com.vigyanmancha.backend.service.StudentClassService;
import com.vigyanmancha.backend.service.SubjectDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject-details")
@Tag(name = "Subject details api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubjectDetailsController {

    private final SubjectDetailsService subjectDetailsService;

    @GetMapping(produces = "application/json")
    public List<SubjectDetailsResponseDto> getAllSubjects() {
        return subjectDetailsService.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public SubjectDetailsResponseDto getSubjectById(@PathVariable Long id) {
        return subjectDetailsService.getById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public SubjectDetailsResponseDto createSubject(@RequestBody SubjectDetailsRequestDTO requestDTO) {
        return subjectDetailsService.create(requestDTO);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public SubjectDetailsResponseDto updateSubject(@RequestBody SubjectDetailsRequestDTO requestDTO) {
        return subjectDetailsService.update(requestDTO);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectDetailsService.delete(id);
    }
}
