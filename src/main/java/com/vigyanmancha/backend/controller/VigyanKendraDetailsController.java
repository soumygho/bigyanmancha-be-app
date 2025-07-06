package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SchoolDetailsResponseDto;
import com.vigyanmancha.backend.service.VigyanKendraDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Vigyankendra deatils API", description = "Endpoints for managing Vigyan kendra.")
@RestController
@RequestMapping("/api/vigyan-kendra")
@RequiredArgsConstructor
public class VigyanKendraDetailsController {
    private final VigyanKendraDetailsService service;

    @GetMapping
    public List<VigyanKendraDetailsRequestDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public VigyanKendraDetailsRequestDTO getById(@PathVariable Long id) {
        return service.getVigyanKendraById(id);
    }

    @GetMapping("/code/{code}")
    public VigyanKendraDetailsRequestDTO getByCode(@PathVariable String code) {
        return service.getVigyanKendraByCode(code);
    }

    @PostMapping
    public VigyanKendraDetailsRequestDTO getVigyanKendraDetails(@RequestBody VigyanKendraDetailsRequestDTO requestDTO) {
        return service.createVigyanKendra(requestDTO);
    }

    @GetMapping("/schools/{id}")
    public Set<SchoolDetailsResponseDto> getSchoolsByCode(@PathVariable Long id) {
        return service.getSchoolDetailsListByVigyanKendraById(id);
    }
}
