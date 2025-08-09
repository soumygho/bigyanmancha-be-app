package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminOrVigyankendraUser;
import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SchoolDetailsResponseDto;
import com.vigyanmancha.backend.service.SchoolDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@Tag(name = "School details api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
public class SchoolDetailsController {
    private final SchoolDetailsService schoolDetailsService;

    @GetMapping(produces = "application/json")
    @AdminOrVigyankendraUser
    public List<SchoolDetailsResponseDto> getAllSchools() {
        return schoolDetailsService.getAll();
    }

    @GetMapping(path="/{id}", produces = "application/json")
    @AdminOrVigyankendraUser
    public SchoolDetailsResponseDto getSchoolById(@PathVariable Long id) {
        return schoolDetailsService.getById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @AdminOrVigyankendraUser
    public SchoolDetailsResponseDto createSchool(@RequestBody SchoolDetailsRequestDTO schoolDTO) {
        return schoolDetailsService.create(schoolDTO);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    @AdminOrVigyankendraUser
    public SchoolDetailsResponseDto updateSchool(@RequestBody SchoolDetailsRequestDTO schoolDTO) {
        return schoolDetailsService.update(schoolDTO.getId(), schoolDTO);
    }

    @GetMapping(path = "/remove-exam-center/{id}", produces = "application/json")
    @AdminOrVigyankendraUser
    public SchoolDetailsResponseDto removeExamCenter(@PathVariable Long id) {
        return schoolDetailsService.removeExamCenter(id);
    }

    @DeleteMapping
    @AdminOrVigyankendraUser
    public void deleteSchool(Long id) {
        schoolDetailsService.delete(id);
    }


}

