package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.*;
import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.dto.request.StudentClassRequestDTO;
import com.vigyanmancha.backend.dto.request.StudentRequestDTO;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.repository.postgres.SchoolDetailsRepository;
import com.vigyanmancha.backend.repository.postgres.StudentClassRepository;
import com.vigyanmancha.backend.repository.postgres.StudentRepository;
import com.vigyanmancha.backend.repository.postgres.VigyanKendraRepository;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import com.vigyanmancha.backend.utility.mapper.StudentDetailsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    //this will only work for single deployment
    private final Object lock = new Object();
    private final StudentRepository studentRepository;
    private final SchoolDetailsRepository schoolDetailsRepository;
    private final StudentClassRepository studentClassRepository;
    private final VigyanKendraRepository vigyanKendraRepository;
    private final VigyanKendraDetailsService vigyanKendraDetailsService;
    private final EnrollmentSessionService enrollmentSessionService;
    private final SchoolDetailsService schoolDetailsService;
    private final StudentClassService studentClassService;


    public List<StudentResponseDto> getAll() {
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyankendra = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            return vigyanKendraDetailsService.getStudentsByVigyanKendraById(vigyankendra.getId());
        }
        return studentRepository.findAll()
                .stream()
                .map(StudentDetailsMapper.studentDetailsMapper::mapFromEntity)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDto> getAllByVigyanKendraId(Long id) {
        return vigyanKendraDetailsService.getStudentsByVigyanKendraById(id);
    }

    public StudentResponseDto getById(Long id) {
        var entity = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        this.validateVigyanKendraUserPermission(entity);
        return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(entity);
    }

    public StudentResponseDto create(StudentRequestDTO dto) {
        VigyanKendraDetails vigyanKendraDetails = vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .orElseThrow(() -> new RuntimeException("Vigyan kendra details not found"));
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyankendra = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            if (!Objects.equals(vigyankendra.getId(), vigyanKendraDetails.getId())) {
                throw new RuntimeException("Not authorized to do any action in this vigyan kendra.");
            }
        }
        EnrollmentSession enrollmentSession = enrollmentSessionService.validateAndGetEnrollmentSessionForCreate();
        SchoolDetails school =
                schoolDetailsRepository.findById(dto.getSchoolId())
                        .orElseThrow(() -> new RuntimeException("School details not found"));
        StudentClass studentClass = studentClassRepository.findById(dto.getStudentClassId())
                .orElseThrow(() -> new RuntimeException("Class details not found"));
        synchronized (lock) {
            Student entity = new Student();
            entity.setName(dto.getName());
            entity.setSex(dto.getSex());
            entity.setSchoolDetails(school);
            entity.setStudentClass(studentClass);
            entity.setRoll(generateRoll(studentClass, vigyanKendraDetails));
            entity.setNumber(generateNumber(studentClass));
            entity.setVigyanKendraDetails(vigyanKendraDetails);
            entity.setEnrollmentSession(enrollmentSession);
            entity = studentRepository.save(entity);
            return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(entity);
        }
    }

    public StudentResponseDto update(StudentRequestDTO dto) {
        Student entity = studentRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        this.validateVigyanKendraUserPermission(entity);
        VigyanKendraDetails vigyanKendraDetails = vigyanKendraRepository.findById(dto.getVigyanKendraId())
                .orElseThrow(() -> new RuntimeException("Vigyan kendra details not found"));
        SchoolDetails school =
                schoolDetailsRepository.findById(dto.getSchoolId())
                        .orElseThrow(() -> new RuntimeException("School details not found"));
        StudentClass studentClass = studentClassRepository.findById(dto.getStudentClassId())
                .orElseThrow(() -> new RuntimeException("Class details not found"));
        entity.setName(dto.getName());
        entity.setSex(dto.getSex());
        entity.setSchoolDetails(school);
        entity.setStudentClass(studentClass);
        if (Objects.nonNull(dto.getRollNumber())) {
            if (studentRepository.getCountByStudentRollAndNumber(entity.getRoll(), dto.getRollNumber().toString()) > 0
                    && !Objects.equals(studentRepository.getByStudentRollAndNumber(entity.getRoll(), dto.getRollNumber().toString()).getId(), entity.getId())) {
                throw new RuntimeException("Roll number already exists");
            }
            entity.setNumber(String.format("%04d", dto.getRollNumber()));
        }
        entity.setVigyanKendraDetails(vigyanKendraDetails);
        return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(studentRepository.save(entity));
    }

    public void delete(Long id) {
        Student entity = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        this.validateVigyanKendraUserPermission(entity);
        studentRepository.deleteById(id);
    }


    private String generateRoll(StudentClass studentClass, VigyanKendraDetails vigyanKendraDetails) {
        return vigyanKendraDetails.getCode() + "/" + studentClass.getName();
    }

    private synchronized String generateNumber(StudentClass studentClass) {
        int count = studentRepository.countAllByStudentClass(studentClass);
        ++count;
        return String.format("%04d", count);
    }

    private void validateVigyanKendraUserPermission(Student studentDetails) {
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyanKendraDetails = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            if (!Objects.equals(studentDetails.getVigyanKendraDetails().getId(), vigyanKendraDetails.getId())) {
                throw new RuntimeException("Not authorized to do any action.");
            }
        }
    }

    public void generateDummyData() {
        var vigyanKendraDto = new VigyanKendraDetailsRequestDTO();
        vigyanKendraDto.setName("perf-vigyan-kendra");
        vigyanKendraDto.setCode("perf-vigyan-kendra");
        var vigyanKendraDetails = vigyanKendraDetailsService.createVigyanKendra(vigyanKendraDto);
        var schoolDetailsDto = new SchoolDetailsRequestDTO();
        schoolDetailsDto.setName("perf-school");
        schoolDetailsDto.setVigyanKendraId(vigyanKendraDetails.getId());
        var schoolDetails = schoolDetailsService.create(schoolDetailsDto);
        var classDto = new StudentClassRequestDTO();
        classDto.setName("perf-class");
        var classDetails = studentClassService.create(classDto);
        CompletableFuture.runAsync(() -> {
            for (int i = 1; i <= 60000; i++) {
                var studentDto = new StudentRequestDTO();
                studentDto.setName("perf-student-" + i);
                studentDto.setStudentClassId(classDetails.getId());
                studentDto.setSchoolId(schoolDetails.getId());
                studentDto.setVigyanKendraId(vigyanKendraDetails.getId());
                studentDto.setSex("M");
                create(studentDto);
                log.info("Added student with name {}", studentDto.getName());
            }
        });
    }
}

