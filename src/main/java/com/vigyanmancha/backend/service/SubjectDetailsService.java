package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.SubjectDetails;
import com.vigyanmancha.backend.dto.request.SubjectDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SubjectDetailsResponseDto;
import com.vigyanmancha.backend.repository.postgres.SubjectDetailsRepository;
import com.vigyanmancha.backend.utility.mapper.SubjectDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectDetailsService {

    private final SubjectDetailsRepository subjectDetailsRepository;

    private SubjectDetailsResponseDto toDto(SubjectDetails entity) {
        return SubjectDetailsMapper.INSTANCE.mapFromEntity(entity);
    }

    public List<SubjectDetailsResponseDto> getAll() {
        return subjectDetailsRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SubjectDetailsResponseDto getById(Long id) {
        return subjectDetailsRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    public void delete(Long id) {
        if(subjectDetailsRepository.existsById(id)) {
            subjectDetailsRepository.deleteById(id);
        }
    }

    public SubjectDetailsResponseDto create(SubjectDetailsRequestDTO dto) {
        SubjectDetails entity = new SubjectDetails();
        entity.setName(dto.getName());
        return toDto(subjectDetailsRepository.save(entity));
    }

    public SubjectDetailsResponseDto update(SubjectDetailsRequestDTO dto) {
        SubjectDetails entity = subjectDetailsRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        entity.setName(dto.getName());
        return toDto(subjectDetailsRepository.save(entity));
    }
}

