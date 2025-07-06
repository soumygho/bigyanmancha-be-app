package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.repository.postgres.SchoolDetailsRepository;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolDetailsService {

    @Autowired
    private SchoolDetailsRepository repository;

    @Autowired
    private VigyanKendraRepository vigyanKendraRepository;

    public List<SchoolDetailsRequestDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SchoolDetailsRequestDTO create(SchoolDetailsRequestDTO dto) {
        SchoolDetails entity = new SchoolDetails();
        entity.setName(dto.getName());

        // Set Vigyan Kendra by code
        vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .ifPresent(entity::setVigyanKendraDetails);

        return toDto(repository.save(entity));
    }

    public SchoolDetailsRequestDTO update(Long id, SchoolDetailsRequestDTO dto) {
        SchoolDetails existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SchoolDetails not found"));

        existing.setName(dto.getName());
        vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .ifPresent(existing::setVigyanKendraDetails);

        return toDto(repository.save(existing));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("SchoolDetails not found");
        }
        repository.deleteById(id);
    }

    // Get school by ID
    public SchoolDetailsRequestDTO getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("School not found"));
    }

    private SchoolDetailsRequestDTO toDto(SchoolDetails entity) {
        SchoolDetailsRequestDTO dto = new SchoolDetailsRequestDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setVigyanKendraId(entity.getVigyanKendraDetails().getId());
        return dto;
    }
}

