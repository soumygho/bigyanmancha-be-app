package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminOrVigyankendraUser;
import com.vigyanmancha.backend.annotation.AdminUser;
import com.vigyanmancha.backend.dto.request.SubjectDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SubjectDetailsResponseDto;
import com.vigyanmancha.backend.service.SubjectDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject-details")
@Tag(name = "Subject details api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
public class SubjectDetailsController {

    private final SubjectDetailsService subjectDetailsService;

    @GetMapping(produces = "application/json")
    @AdminOrVigyankendraUser
    public List<SubjectDetailsResponseDto> getAllSubjects() {
        return subjectDetailsService.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @AdminOrVigyankendraUser
    public SubjectDetailsResponseDto getSubjectById(@PathVariable Long id) {
        return subjectDetailsService.getById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public SubjectDetailsResponseDto createSubject(@RequestBody SubjectDetailsRequestDTO requestDTO) {
        return subjectDetailsService.create(requestDTO);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public SubjectDetailsResponseDto updateSubject(@RequestBody SubjectDetailsRequestDTO requestDTO) {
        return subjectDetailsService.update(requestDTO);
    }

    @DeleteMapping(path = "/{id}")
    @AdminUser
    public void deleteSubject(@PathVariable Long id) {
        subjectDetailsService.delete(id);
    }
}
