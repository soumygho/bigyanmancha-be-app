package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.EnrollmentSession;
import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SchoolDetailsResponseDto;
import com.vigyanmancha.backend.repository.postgres.SchoolDetailsRepository;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolDetailsService {
    private final SchoolDetailsRepository repository;
    private final VigyanKendraRepository vigyanKendraRepository;
    private final VigyanKendraDetailsService vigyanKendraDetailsService;
    private final EnrollmentSessionService enrollmentSessionService;
    @PersistenceContext
    private EntityManager entityManager;

    public List<SchoolDetailsResponseDto> getAll() {
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyanKendraDetails = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            return vigyanKendraDetailsService.getSchoolDetailsListByVigyanKendraById(vigyanKendraDetails.getId());
        }
        return repository.findAll()
                .stream()
                .map(SchoolDetailsService::toDto)
                .collect(Collectors.toList());
    }

    public SchoolDetailsResponseDto create(SchoolDetailsRequestDTO dto) {
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyanKendraDetails = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            if (!Objects.equals(vigyanKendraDetails.getId(), dto.getVigyanKendraId())) {
                throw new RuntimeException("Not authorized to do any action in this vigyan kendra.");
            }
        }
        EnrollmentSession enrollmentSession = enrollmentSessionService.validateAndGetEnrollmentSessionForCreate();
        SchoolDetails entity = new SchoolDetails();
        entity.setEnrollmentSession(enrollmentSession);
        entity.setName(dto.getName());
        // Set Vigyan Kendra by code
        if (!vigyanKendraRepository.existsById(dto.getVigyanKendraId())) {
            throw new RuntimeException("VigyanKendra not found.");
        }
        vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .ifPresent(entity::setVigyanKendraDetails);
        return toDto(repository.save(entity));
    }

    public SchoolDetailsResponseDto update(Long id, SchoolDetailsRequestDTO dto) {
        SchoolDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SchoolDetails not found"));
        this.validateVigyanKendraUserPermission(existing);
        existing.setName(dto.getName());
        vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .ifPresent(existing::setVigyanKendraDetails);

        return toDto(repository.save(existing));
    }

    public SchoolDetailsResponseDto removeExamCenter(Long id) {
        SchoolDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SchoolDetails not found"));
        this.validateVigyanKendraUserPermission(existing);
        existing.setExaminationCentre(null);
        return toDto(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        SchoolDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SchoolDetails not found"));
        this.validateVigyanKendraUserPermission(existing);
        if (Objects.equals(existing.getId(),
                existing.getExaminationCentre().getDetails().getId())) {
            existing.setExaminationCentre(null);
            repository.save(existing);
        }
        entityManager.detach(existing);
        existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SchoolDetails not found"));
        repository.delete(existing);
    }

    // Get school by ID
    public SchoolDetailsResponseDto getById(Long id) {
        var schoolDetails = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));
        this.validateVigyanKendraUserPermission(schoolDetails);
        return SchoolDetailsService.toDto(schoolDetails);
    }

    public static SchoolDetailsResponseDto toDto(SchoolDetails entity) {
        SchoolDetailsResponseDto dto = new SchoolDetailsResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setVigyanKendraId(entity.getVigyanKendraDetails().getId());
        dto.setVigyanKendraName(entity.getVigyanKendraDetails().getName());
        dto.setEnrollmentId(entity.getEnrollmentSession().getId());
        dto.setEnrollmentYear(entity.getEnrollmentSession().getYear());
        if (Objects.nonNull(entity.getExaminationCentre())) {
            dto.setExamCentreName(entity.getExaminationCentre().getName());
            dto.setExamCentreId(entity.getExaminationCentre().getId());
        }

        return dto;
    }

    private void validateVigyanKendraUserPermission(SchoolDetails schoolDetails) {
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyanKendraDetails = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            if (!Objects.equals(schoolDetails.getVigyanKendraDetails().getId(), vigyanKendraDetails.getId())) {
                throw new RuntimeException("Not authorized to do any action.");
            }
        }
    }
}

