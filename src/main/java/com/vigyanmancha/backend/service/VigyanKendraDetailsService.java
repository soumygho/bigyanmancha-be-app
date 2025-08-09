package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.*;
import com.vigyanmancha.backend.dto.request.ExaminationCentreDetailsRequestDTO;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.SchoolDetailsResponseDto;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.exceptions.ConflictException;
import com.vigyanmancha.backend.repository.postgres.StudentRepository;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import com.vigyanmancha.backend.utility.VigyanKendraDetailsMapper;

import com.vigyanmancha.backend.utility.mapper.StudentDetailsMapper;
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
    private final StudentRepository studentRepository;

    public VigyanKendraDetailsRequestDTO createVigyanKendra(VigyanKendraDetailsRequestDTO dto) {
        if(checkUniqueCode(dto.getCode())) {
            throw  new ConflictException("Code should be unique.");
        }
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

    public List<SchoolDetailsResponseDto> getSchoolDetailsListByVigyanKendraById(Long id) {
        var vigyanKendraDetails =  repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        Set<SchoolDetails> schoolDetailsList =  vigyanKendraDetails.getSchoolDetails();
        if(Objects.isNull(schoolDetailsList)) {
            return Collections.emptyList();
        }
        return schoolDetailsList.stream().map(SchoolDetailsService::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDto> getStudentsByVigyanKendraById(Long id) {
        var vigyanKendraDetails =  repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        List<Student> studentsList =  studentRepository.findByVigyanKendra(vigyanKendraDetails.getId());
        if(Objects.isNull(studentsList)) {
            return Collections.emptyList();
        }
        return studentsList.stream()
                .map(StudentDetailsMapper.studentDetailsMapper::mapFromEntity)
                .collect(Collectors.toList());
    }

    public List<ExaminationCentreDetailsRequestDTO> getExaminationcentersByVigyanKendraId(Long id) {
        var vigyanKendraDetails =  repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        Set<ExaminationCentreDetails> studentsList =  vigyanKendraDetails.getExaminationCentres();
        if(Objects.isNull(studentsList)) {
            return Collections.emptyList();
        }
        return studentsList.stream()
                .map(ExaminationCentreDetailsService::toDto)
                .collect(Collectors.toList());
    }

    public VigyanKendraDetails existOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
    }

    public Set<UserDetails> getUserDetailsListByVigyanKendraById(Long id) {
        var vigyanKendraDetails =  repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        Set<UserDetails> userDetailsList =  vigyanKendraDetails.getUsers();
        if(Objects.isNull(userDetailsList)) {
            return Collections.emptySet();
        }
        return userDetailsList;
    }

    public VigyanKendraDetailsRequestDTO updateVigyanKendra(VigyanKendraDetailsRequestDTO dto) {
        VigyanKendraDetails entity = repository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        if(checkUniqueCodeForUpdate(dto.getCode(), dto.getId())) {
            throw  new ConflictException("Code should be unique.");
        }
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        VigyanKendraDetails saved = repository.save(entity);
        return VigyanKendraDetailsMapper.toDTO(saved);
    }

    public void deleteVigyanKendraById(Long id) {
        VigyanKendraDetails entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VigyanKendra not found"));
        repository.delete(entity);
    }


    public VigyanKendraDetailsRequestDTO getVigyanKendraFromAuth() {
        if (RoleUtility.isVigyanKendraUser()) {
            Long vigyanKendraId = RoleUtility.getVigyanKendraId();
            var vigyanKendraDetails =  existOrThrow(vigyanKendraId);
            return VigyanKendraDetailsMapper.toDTO(vigyanKendraDetails);
        }
        return null;
    }


    private boolean checkUniqueCode(String code) {
        return repository.findAll()
                .stream().anyMatch(record -> code.equalsIgnoreCase(record.getCode()));
    }

    private boolean checkUniqueCodeForUpdate(String code, Long id) {
        return repository.findAll()
                .stream().anyMatch(record -> !Objects.equals(record.getId(), id)
                        && code.equalsIgnoreCase(record.getCode()));
    }
}
