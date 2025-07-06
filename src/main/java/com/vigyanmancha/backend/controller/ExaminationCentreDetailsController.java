package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.ExaminationCentreDetailsRequestDTO;
import com.vigyanmancha.backend.service.ExaminationCentreDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/examination-centres")
@Tag(name = "Examination centre details api")
@RequiredArgsConstructor
public class ExaminationCentreDetailsController {
    private final ExaminationCentreDetailsService service;

    // Get all ExaminationCentreDetails
    @GetMapping
    public List<ExaminationCentreDetailsRequestDTO> getAll() {
        return service.getAll();
    }

    // Get ExaminationCentreDetails by id
    @GetMapping("/{id}")
    public ExaminationCentreDetailsRequestDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create ExaminationCentreDetails
    @PostMapping
    public ExaminationCentreDetailsRequestDTO create(@RequestBody ExaminationCentreDetailsRequestDTO dto) {
        return service.create(dto);
    }

    // Update ExaminationCentreDetails
    @PutMapping("/{id}")
    public ExaminationCentreDetailsRequestDTO update(@PathVariable Long id, @RequestBody ExaminationCentreDetailsRequestDTO dto) {
        return service.update(id, dto);
    }

    // Delete ExaminationCentreDetails
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

