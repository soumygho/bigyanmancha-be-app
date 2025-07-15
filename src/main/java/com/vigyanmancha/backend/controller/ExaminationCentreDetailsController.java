package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.ExaminationCentreDetailsRequestDTO;
import com.vigyanmancha.backend.dto.request.SchoolAssignDeAssignRequest;
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
@CrossOrigin(origins = "*")
public class ExaminationCentreDetailsController {
    private final ExaminationCentreDetailsService service;

    // Get all ExaminationCentreDetails
    @GetMapping(produces = "application/json")
    public List<ExaminationCentreDetailsRequestDTO> getAllExamCenters() {
        return service.getAll();
    }

    // Get ExaminationCentreDetails by id
    @GetMapping(path="/{id}", produces = "application/json")
    public ExaminationCentreDetailsRequestDTO getExamCenterById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Create ExaminationCentreDetails
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ExaminationCentreDetailsRequestDTO createExamCenter(@RequestBody ExaminationCentreDetailsRequestDTO dto) {
        return service.create(dto);
    }

    // Update ExaminationCentreDetails
    @PutMapping(produces = "application/json", consumes = "application/json")
    public ExaminationCentreDetailsRequestDTO updateExamCenter(@RequestBody ExaminationCentreDetailsRequestDTO dto) {
        return service.update(dto.getId(), dto);
    }

    // Delete ExaminationCentreDetails
    @DeleteMapping(path="/{id}")
    public void deleteExamCenter(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping(path = "/assign", produces = "application/json", consumes = "application/json")
    public ExaminationCentreDetailsRequestDTO assignSchool(@RequestBody SchoolAssignDeAssignRequest dto) {
        return service.assignSchoolToExamCenter(dto);
    }

    @PostMapping(path = "/de-assign", produces = "application/json", consumes = "application/json")
    public ExaminationCentreDetailsRequestDTO deAssignSchool(@RequestBody SchoolAssignDeAssignRequest dto) {
        return service.deAssignSchoolToExamCenter(dto);
    }
}

