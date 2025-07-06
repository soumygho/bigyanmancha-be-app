package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import com.vigyanmancha.backend.domain.postgres.VigyanKendraDetails;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SchoolDetailsResponseDto;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import com.vigyanmancha.backend.utility.mapper.SchoolDetailsMapper;
import com.vigyanmancha.backend.utility.VigyanKendraDetailsMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VigyanKendraDetailsService {

    private final VigyanKendraRepository repository;

    public VigyanKendraDetailsRequestDTO createVigyanKendra(VigyanKendraDetailsRequestDTO dto) {

        VigyanKendraDetails entity = new VigyanKendraDetails();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        VigyanKendraDetails saved = repository.save(entity);
        return VigyanKendraDetailsMapper.toDTO(saved);
    }

    public List<VigyanKendraDetailsRequestDTO> getAll() {
        return repository.findAll().stream()
                .map(VigyanKendraDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VigyanKendraDetailsRequestDTO getVigyanKendraById(Long id) {
        return repository.findById(id)
                .map(VigyanKendraDetailsMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
    }

    public VigyanKendraDetailsRequestDTO getVigyanKendraByCode(String code) {
        return repository.findByCode(code)
                .map(VigyanKendraDetailsMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
    }

    public Set<SchoolDetailsResponseDto> getSchoolDetailsListByVigyanKendraById(Long id) {
        var vigyanKendraDetails =  repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        Set<SchoolDetails> schoolDetailsList =  vigyanKendraDetails.getSchoolDetails();
        if(Objects.isNull(schoolDetailsList)) {
            return Collections.emptySet();
        }
        return schoolDetailsList.stream()
                .map(SchoolDetailsMapper.INSTANCE::mapFromEntity)
                .collect(Collectors.toSet());
    }
}
