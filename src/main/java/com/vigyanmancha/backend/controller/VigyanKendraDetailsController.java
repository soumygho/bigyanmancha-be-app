package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.domain.postgres.UserDetails;
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
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VigyanKendraDetailsController {
    private final VigyanKendraDetailsService service;

    @GetMapping(produces = "application/json")
    public List<VigyanKendraDetailsRequestDTO> getAllVigyanKendras() {
        return service.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public VigyanKendraDetailsRequestDTO getVigyanKendraById(@PathVariable Long id) {
        return service.getVigyanKendraById(id);
    }

    @GetMapping(path = "/code/{code}", produces = "application/json", consumes = "application/json")
    public VigyanKendraDetailsRequestDTO getVigyanKendraByCode(@PathVariable String code) {
        return service.getVigyanKendraByCode(code);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public VigyanKendraDetailsRequestDTO createVigyanKendraDetails(@RequestBody VigyanKendraDetailsRequestDTO requestDTO) {
        return service.createVigyanKendra(requestDTO);
    }

    @GetMapping(path = "/schools/{id}", produces = "application/json")
    public Set<SchoolDetailsResponseDto> getSchoolsByVigyanKendraId(@PathVariable Long id) {
        return service.getSchoolDetailsListByVigyanKendraById(id);
    }

    @GetMapping(path = "/users/{id}", produces = "application/json")
    public Set<UserDetails> getUsersByVigyanKendraId(@PathVariable Long id) {
        return service.getUserDetailsListByVigyanKendraById(id);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    public VigyanKendraDetailsRequestDTO updateVigyanKendraDetails(@RequestBody VigyanKendraDetailsRequestDTO requestDTO) {
        return service.updateVigyanKendra(requestDTO);
    }

    @DeleteMapping
    public void deleteVigyanKendraById(Long id) {
        service.deleteVigyanKendraById(id);
    }
}
