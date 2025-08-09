package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminOrVigyankendraOrSchoolUser;
import com.vigyanmancha.backend.annotation.AdminOrVigyankendraUser;
import com.vigyanmancha.backend.annotation.AdminUser;
import com.vigyanmancha.backend.domain.postgres.UserDetails;
import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SchoolDetailsResponseDto;
import com.vigyanmancha.backend.service.VigyanKendraDetailsService;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Vigyankendra deatils API", description = "Endpoints for managing Vigyan kendra.")
@RestController
@RequestMapping("/api/vigyan-kendra")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
@Slf4j
public class VigyanKendraDetailsController {
    private final VigyanKendraDetailsService service;

    @GetMapping(produces = "application/json")
    @AdminOrVigyankendraUser
    public List<VigyanKendraDetailsRequestDTO> getAllVigyanKendras() {
        if (RoleUtility.isVigyanKendraUser()) {
            return List.of(service.getVigyanKendraFromAuth());
        }
        log.info("Is user admin ? : " + RoleUtility.isAdminUser());
        return service.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @AdminOrVigyankendraUser
    public VigyanKendraDetailsRequestDTO getVigyanKendraById(@PathVariable Long id) {
        if (RoleUtility.isVigyanKendraUser()) {
            return service.getVigyanKendraFromAuth();
        }
        return service.getVigyanKendraById(id);
    }

    @GetMapping(path = "/code/{code}", produces = "application/json", consumes = "application/json")
    @AdminOrVigyankendraUser
    public VigyanKendraDetailsRequestDTO getVigyanKendraByCode(@PathVariable String code) {
        if (RoleUtility.isVigyanKendraUser()) {
            return service.getVigyanKendraFromAuth();
        }
        return service.getVigyanKendraByCode(code);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public VigyanKendraDetailsRequestDTO createVigyanKendraDetails(@RequestBody VigyanKendraDetailsRequestDTO requestDTO) {
        return service.createVigyanKendra(requestDTO);
    }

    @GetMapping(path = "/schools/{id}", produces = "application/json")
    @AdminOrVigyankendraUser
    public List<SchoolDetailsResponseDto> getSchoolsByVigyanKendraId(@PathVariable Long id) {
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyanKendraDetails  = service.getVigyanKendraFromAuth();
            return service.getSchoolDetailsListByVigyanKendraById(vigyanKendraDetails.getId());
        }
        return service.getSchoolDetailsListByVigyanKendraById(id);
    }

    @GetMapping(path = "/users/{id}", produces = "application/json")
    @AdminUser
    public Set<UserDetails> getUsersByVigyanKendraId(@PathVariable Long id) {
        return service.getUserDetailsListByVigyanKendraById(id);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public VigyanKendraDetailsRequestDTO updateVigyanKendraDetails(@RequestBody VigyanKendraDetailsRequestDTO requestDTO) {
        return service.updateVigyanKendra(requestDTO);
    }

    @DeleteMapping
    @AdminUser
    public void deleteVigyanKendraById(Long id) {
        service.deleteVigyanKendraById(id);
    }
}
