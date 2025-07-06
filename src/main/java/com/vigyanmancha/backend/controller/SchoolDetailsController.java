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
@RequiredArgsConstructor
public class SchoolDetailsController {
    private final SchoolDetailsService schoolDetailsService;

    @GetMapping
    public List<SchoolDetailsRequestDTO> getAllSchools() {
        return schoolDetailsService.getAll();
    }

    @GetMapping("/{id}")
    public SchoolDetailsRequestDTO getSchoolById(@PathVariable Long id) {
        return schoolDetailsService.getById(id);
    }

    @PostMapping
    public SchoolDetailsRequestDTO createSchool(@RequestBody SchoolDetailsRequestDTO schoolDTO) {
        return schoolDetailsService.create(schoolDTO);
    }
}

