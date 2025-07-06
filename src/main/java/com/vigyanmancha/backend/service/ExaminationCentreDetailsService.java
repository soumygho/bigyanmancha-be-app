package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.ExaminationCentreDetails;
import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import com.vigyanmancha.backend.dto.request.ExaminationCentreDetailsRequestDTO;
import com.vigyanmancha.backend.repository.postgres.ExaminationCentreDetailsRepository;
import com.vigyanmancha.backend.repository.postgres.SchoolDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExaminationCentreDetailsService {

    @Autowired
    private ExaminationCentreDetailsRepository repository;

    @Autowired
    private SchoolDetailsRepository schoolDetailsRepository;

    // Create ExaminationCentreDetails
    public ExaminationCentreDetailsRequestDTO create(ExaminationCentreDetailsRequestDTO dto) {
        ExaminationCentreDetails entity = new ExaminationCentreDetails();
        entity.setName(dto.getName());

        // Set the details field (one-to-one with SchoolDetails)
        Optional<SchoolDetails> details = schoolDetailsRepository.findById(dto.getSchoolDetailsId());
        details.ifPresent(entity::setDetails);

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
        dto.setSchoolDetailsId(entity.getDetails() != null ? entity.getDetails().getId() : null);
        return dto;
    }

    // Update ExaminationCentreDetails
    public ExaminationCentreDetailsRequestDTO update(Long id, ExaminationCentreDetailsRequestDTO dto) {
        ExaminationCentreDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Examination Centre not found"));

        existing.setName(dto.getName());

        Optional<SchoolDetails> details = schoolDetailsRepository.findById(dto.getSchoolDetailsId());
        details.ifPresent(existing::setDetails);

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

