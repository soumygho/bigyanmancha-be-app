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
@RequiredArgsConstructor
public class SubjectDetailsController {

    private final SubjectDetailsService subjectDetailsService;

    @GetMapping
    public List<SubjectDetailsResponseDto> getAll() {
        return subjectDetailsService.getAll();
    }

    @GetMapping("/{id}")
    public SubjectDetailsResponseDto getById(@PathVariable Long id) {
        return subjectDetailsService.getById(id);
    }

    @PostMapping
    public SubjectDetailsResponseDto create(@RequestBody SubjectDetailsRequestDTO requestDTO) {
        return subjectDetailsService.create(requestDTO);
    }

    @PutMapping
    public SubjectDetailsResponseDto update(@RequestBody SubjectDetailsRequestDTO requestDTO) {
        return subjectDetailsService.update(requestDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        subjectDetailsService.delete(id);
    }
}
