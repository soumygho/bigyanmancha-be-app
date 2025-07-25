package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.ExaminationCentreDetails;
import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import com.vigyanmancha.backend.domain.postgres.VigyanKendraDetails;
import com.vigyanmancha.backend.dto.request.ExaminationCentreDetailsRequestDTO;
import com.vigyanmancha.backend.dto.request.SchoolAssignDeAssignRequest;
import com.vigyanmancha.backend.repository.postgres.ExaminationCentreDetailsRepository;
import com.vigyanmancha.backend.repository.postgres.SchoolDetailsRepository;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExaminationCentreDetailsService {

    @Autowired
    private ExaminationCentreDetailsRepository repository;

    @Autowired
    private SchoolDetailsRepository schoolDetailsRepository;

    @Autowired
    private VigyanKendraRepository vigyanKendraRepository;

    // Create ExaminationCentreDetails
    public ExaminationCentreDetailsRequestDTO create(ExaminationCentreDetailsRequestDTO dto) {
        ExaminationCentreDetails entity = new ExaminationCentreDetails();
        entity.setName(dto.getName());

        // Set the details field (one-to-one with SchoolDetails)
        SchoolDetails details = schoolDetailsRepository.findById(dto.getSchoolDetailsId())
                .orElseThrow(() -> new RuntimeException("School details not found"));
        entity.setDetails(details);

        var vigyanKendraDetails =
                vigyanKendraRepository.findById(dto.getVigyanKendraId()).orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        entity.setVigyanKendraDetails(vigyanKendraDetails);

        // Save the entity
        return toDto(repository.save(entity));
    }

    // Get all ExaminationCentreDetails
    public List<ExaminationCentreDetailsRequestDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Get ExaminationCentreDetails by id
    public ExaminationCentreDetailsRequestDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Examination Centre not found"));
    }

    // Convert entity to DTO
    private ExaminationCentreDetailsRequestDTO toDto(ExaminationCentreDetails entity) {
        ExaminationCentreDetailsRequestDTO dto = new ExaminationCentreDetailsRequestDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        if (Objects.nonNull(entity.getDetails())) {
            dto.setSchoolName(entity.getDetails().getName());
            dto.setSchoolDetailsId(entity.getDetails().getId());
        }

        if (Objects.nonNull(entity.getVigyanKendraDetails())) {
            dto.setVigyanKendraId(entity.getVigyanKendraDetails().getId());
            dto.setVigyanKendraName(entity.getVigyanKendraDetails().getName());
            dto.setVigyanKendraCode(entity.getVigyanKendraDetails().getCode());
        }
        if (!CollectionUtils.isEmpty(entity.getSchools())) {
            var schoolNames = entity.getSchools().stream()
                    .map(SchoolDetails::getName)
                    .collect(Collectors.toSet());
            dto.setSchoolNames(schoolNames);
        } else {
            dto.setSchoolNames(Collections.emptySet());
        }

        return dto;
    }

    // Update ExaminationCentreDetails
    public ExaminationCentreDetailsRequestDTO update(Long id, ExaminationCentreDetailsRequestDTO dto) {
        ExaminationCentreDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Examination Centre not found"));

        existing.setName(dto.getName());

        // Set the details field (one-to-one with SchoolDetails)
        SchoolDetails details = schoolDetailsRepository.findById(dto.getSchoolDetailsId())
                .orElseThrow(() -> new RuntimeException("School details not found"));
        existing.setDetails(details);

        var vigyanKendraDetails =
                vigyanKendraRepository.findById(dto.getVigyanKendraId()).orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        existing.setVigyanKendraDetails(vigyanKendraDetails);

        return toDto(repository.save(existing));
    }

    public ExaminationCentreDetailsRequestDTO assignSchoolToExamCenter(SchoolAssignDeAssignRequest request) {
        final ExaminationCentreDetails existing = repository.findById(request.getExaminationCentreId())
                .orElseThrow(() -> new RuntimeException("Examination Centre not found"));
        request.getSchoolIds().forEach(schoolId -> {
            var schoolDetails = schoolDetailsRepository.findById(schoolId)
                            .orElseThrow(() -> new RuntimeException("School details not found"));
            schoolDetails.setExaminationCentre(existing);
            schoolDetailsRepository.save(schoolDetails);
        });
        var entity = repository.findById(request.getExaminationCentreId())
                .orElseThrow(() -> new RuntimeException("Examination Centre not found"));
        return toDto(repository.save(entity));
    }

    public ExaminationCentreDetailsRequestDTO deAssignSchoolToExamCenter(SchoolAssignDeAssignRequest request) {
        ExaminationCentreDetails existing = repository.findById(request.getExaminationCentreId())
                .orElseThrow(() -> new RuntimeException("Examination Centre not found"));
        Set<SchoolDetails> schoolDetailsSet = !CollectionUtils.isEmpty(existing.getSchools())
                ? existing.getSchools() : new HashSet<>();
        request.getSchoolIds()
                .forEach(id -> {
                    var schoolDetails = schoolDetailsRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("School details not found"));
                    schoolDetailsSet.remove(schoolDetails);
                });
        existing.setSchools(schoolDetailsSet);
        return toDto(repository.save(existing));
    }

    // Delete ExaminationCentreDetails
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Examination Centre not found");
        }
        repository.deleteById(id);
    }
}

