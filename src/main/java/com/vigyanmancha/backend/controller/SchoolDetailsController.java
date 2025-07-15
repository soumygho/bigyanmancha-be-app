package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.service.SchoolDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@Tag(name = "School details api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SchoolDetailsController {
    private final SchoolDetailsService schoolDetailsService;

    @GetMapping(produces = "application/json")
    public List<SchoolDetailsRequestDTO> getAllSchools() {
        return schoolDetailsService.getAll();
    }

    @GetMapping(path="/{id}", produces = "application/json")
    public SchoolDetailsRequestDTO getSchoolById(@PathVariable Long id) {
        return schoolDetailsService.getById(id);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public SchoolDetailsRequestDTO createSchool(@RequestBody SchoolDetailsRequestDTO schoolDTO) {
        return schoolDetailsService.create(schoolDTO);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public SchoolDetailsRequestDTO updateSchool(@RequestBody SchoolDetailsRequestDTO schoolDTO) {
        return schoolDetailsService.update(schoolDTO.getId(), schoolDTO);
    }

    @DeleteMapping
    public void deleteSchool(Long id) {
        schoolDetailsService.delete(id);
    }
}

