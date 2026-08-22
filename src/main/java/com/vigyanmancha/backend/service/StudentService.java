package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.*;
import com.vigyanmancha.backend.dto.reporting.StatisticsReportDto;
import com.vigyanmancha.backend.dto.request.SchoolDetailsRequestDTO;
import com.vigyanmancha.backend.dto.request.StudentClassRequestDTO;
import com.vigyanmancha.backend.dto.request.StudentRequestDTO;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.DRSheetResponse;
import com.vigyanmancha.backend.dto.response.EnrollmentCountResponse;
import com.vigyanmancha.backend.dto.response.StudentDetails;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.repository.postgres.*;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import com.vigyanmancha.backend.utility.mapper.StudentDetailsMapper;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final ExaminationCentreDetailsRepository examinationCentreDetailsRepository;
    //this will only work for single deployment
    private final StudentRepository studentRepository;
    private final SchoolDetailsRepository schoolDetailsRepository;
    private final StudentClassRepository studentClassRepository;
    private final VigyanKendraRepository vigyanKendraRepository;
    private final VigyanKendraDetailsService vigyanKendraDetailsService;
    private final EnrollmentSessionService enrollmentSessionService;
    private final SchoolDetailsService schoolDetailsService;
    private final StudentClassService studentClassService;
    private final ExaminationCentreDetailsService examinationCentreDetailsService;
    private final Map<String, Integer> weightedClass = new HashMap<>();
    private final Map<Integer, String> reverseWeightedClass = new HashMap<>();

    @PostConstruct
    void populateWeightedClass() {
        weightedClass.put("I", 1);
        weightedClass.put("II", 2);
        weightedClass.put("III", 3);
        weightedClass.put("IV", 4);
        weightedClass.put("V", 5);
        weightedClass.put("VI", 6);
        weightedClass.put("VII", 7);
        weightedClass.put("VIII", 8);
        weightedClass.put("IX", 9);
        weightedClass.put("X", 10);
        weightedClass.put("XI", 11);
        weightedClass.put("XII", 12);
        reverseWeightedClass.put(1, "I");
        reverseWeightedClass.put(2, "II");
        reverseWeightedClass.put(3, "III");
        reverseWeightedClass.put(4, "IV");
        reverseWeightedClass.put(5, "V");
        reverseWeightedClass.put(6, "VI");
        reverseWeightedClass.put(7, "VII");
        reverseWeightedClass.put(8, "VIII");
        reverseWeightedClass.put(9, "IX");
        reverseWeightedClass.put(10, "X");
        reverseWeightedClass.put(11, "XI");
        reverseWeightedClass.put(12, "XII");
    }

    //TODO: needs pagination for smooth experience
    public List<StudentResponseDto> getAll() {
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyankendra = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            return vigyanKendraDetailsService.getStudentsByVigyanKendraById(vigyankendra.getId());
        }
        /*return studentRepository.findAll()
                .stream()
                .map(StudentDetailsMapper.studentDetailsMapper::mapFromEntity)
                .collect(Collectors.toList());*/
        return Collections.emptyList();
    }

    public List<StudentResponseDto> getAllByVigyanKendraId(Long id) {
        return vigyanKendraDetailsService.getStudentsByVigyanKendraById(id);
    }

    public List<StudentResponseDto> getAllByVigyanKendraIdAndEnrollmentSession(Long vigyanKendraId,
                                                                               EnrollmentSession enrollmentSession,
                                                                               @Nullable ExaminationCentreDetails examinationCentreDetails,
                                                                               @Nullable StudentClass studentClass) {
        if (Objects.isNull(vigyanKendraId)) {
            throw new RuntimeException("VigyanKendra Id is required.");
        }
        if (Objects.isNull(enrollmentSession)) {
            throw new RuntimeException("Enrollment Session is required.");
        }
        return getStudentsByVigyanKendraAndEnrollmentYearOrExamCenterOrClass(vigyanKendraId, enrollmentSession, examinationCentreDetails, studentClass);
    }

        private List<StudentResponseDto> getStudentsByVigyanKendraAndEnrollmentYearOrExamCenterOrClass(Long vigyanKendraId,
                                                                                                  EnrollmentSession enrollmentSession,
                                                                                                  ExaminationCentreDetails examinationCentreDetails,
                                                                                                  StudentClass studentClass) {
        List<Student> studentsList ;
        if(Objects.nonNull(examinationCentreDetails) && Objects.nonNull(studentClass)) {
            studentsList =  studentRepository.findByVigyanKendraAndEnrollmentSessionAndExamCenterAndClass(
                    vigyanKendraId, enrollmentSession, examinationCentreDetails, studentClass);

        } else if(Objects.nonNull(examinationCentreDetails)) {
            studentsList =  studentRepository.findByVigyanKendraAndEnrollmentSessionAndExamCenter(
                    vigyanKendraId, enrollmentSession, examinationCentreDetails);
        } else if(Objects.nonNull(studentClass)) {
            studentsList =  studentRepository.findByVigyanKendraAndEnrollmentSessionAndClass(
                    vigyanKendraId, enrollmentSession, studentClass);
        } else {
            studentsList =  studentRepository.findByVigyanKendraAndEnrollmentSession(
                    vigyanKendraId, enrollmentSession);
        }
        if(Objects.isNull(studentsList)) {
            return Collections.emptyList();
        }
        return studentsList.stream()
                .map(StudentDetailsMapper.studentDetailsMapper::mapFromEntity)
                .collect(Collectors.toList());
    }

    public List<DRSheetResponse> getDRSheetData(@NotNull Long vigyanKendraId, @Nullable Long classId, @Nullable Long examCenterId) {
        log.info("Fetching DR Sheet data for vigyanKendraId: {}, classId: {}, examCenterId: {}", vigyanKendraId, classId, examCenterId);
        if (Objects.isNull(vigyanKendraId)) {
            throw new RuntimeException("VigyanKendra Id is required.");
        }
        var vigyanKendraDetails = vigyanKendraDetailsService.getVigyanKendraById(vigyanKendraId);
        ExaminationCentreDetails examinationCentreDetails = null;
        StudentClass studentClass = null;
        if (Objects.nonNull(classId) && classId != 0) {
            studentClass = studentClassRepository.findById(classId).orElseThrow(() -> new RuntimeException("Student Class not found."));
        }
        if (Objects.nonNull(examCenterId) && examCenterId != 0) {
            examinationCentreDetails = examinationCentreDetailsRepository.findById(examCenterId).orElseThrow(() -> new RuntimeException("Examination Centre not found."));
        }
        List<StudentResponseDto> studentResponseDtos =
                getAllByVigyanKendraIdAndEnrollmentSession(vigyanKendraId, enrollmentSessionService.getActiveEnrollmentSession(), examinationCentreDetails, studentClass);
        return studentResponseDtos.stream()
                .collect(Collectors.groupingBy(student -> Arrays.asList(student.getClassId().toString(), student.getExaminationCentreId().toString(), student.getClassName(), student.getExaminationCentreName())))
                .entrySet()
                .stream()
                .map(entry -> {
                    List<String> key = entry.getKey();
                    List<StudentDetails> studentDetails = entry.getValue()
                            .stream()
                            .map(st -> StudentDetails.builder()
                                    .no(st.getNumber())
                                    .name(st.getName())
                                    .roll(st.getRoll())
                                    .build()).collect(Collectors.toUnmodifiableList());
                    DRSheetResponse resp = DRSheetResponse.builder()
                            .students(studentDetails)
                            .className(key.get(2))
                            .examCenterName(key.get(3))
                            .vigyanKendraCode(vigyanKendraDetails.getCode())
                            .vigyanKendraName(vigyanKendraDetails.getName())
                            .build();
                    return resp;
                }).collect(Collectors.toUnmodifiableList());
    }

    public StudentResponseDto getById(Long id) {
        var entity = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        this.validateVigyanKendraUserPermission(entity);
        return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(entity);
    }

    public StudentResponseDto create(StudentRequestDTO dto) {
        VigyanKendraDetails vigyanKendraDetails = vigyanKendraRepository.findById(dto.getVigyanKendraId()).orElseThrow(() -> new RuntimeException("Vigyan kendra details not found"));
        if (RoleUtility.isVigyanKendraUser()) {
            var vigyankendra = vigyanKendraDetailsService.getVigyanKendraFromAuth();
            if (!Objects.equals(vigyankendra.getId(), vigyanKendraDetails.getId())) {
                throw new RuntimeException("Not authorized to do any action in this vigyan kendra.");
            }
        }
        EnrollmentSession enrollmentSession = enrollmentSessionService.validateAndGetEnrollmentSessionForCreate();
        SchoolDetails school = schoolDetailsRepository.findById(dto.getSchoolId()).orElseThrow(() -> new RuntimeException("School details not found"));
        StudentClass studentClass = studentClassRepository.findById(dto.getStudentClassId()).orElseThrow(() -> new RuntimeException("Class details not found"));
        Student entity = new Student();
        entity.setName(dto.getName());
        entity.setSex(dto.getSex());
        entity.setSchoolDetails(school);
        entity.setStudentClass(studentClass);
        entity.setRoll(generateRoll(studentClass, vigyanKendraDetails));
        entity.setNumber("NA");
        entity.setVigyanKendraDetails(vigyanKendraDetails);
        entity.setEnrollmentSession(enrollmentSession);
        entity = studentRepository.save(entity);
        return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(entity);
    }

    public StudentResponseDto update(StudentRequestDTO dto) {
        this.enrollmentSessionService.validateAndGetEnrollmentSessionForModification();
        Student entity = studentRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Student not found"));
        this.validateVigyanKendraUserPermission(entity);
        VigyanKendraDetails vigyanKendraDetails = vigyanKendraRepository.findById(dto.getVigyanKendraId()).orElseThrow(() -> new RuntimeException("Vigyan kendra details not found"));
        SchoolDetails school = schoolDetailsRepository.findById(dto.getSchoolId()).orElseThrow(() -> new RuntimeException("School details not found"));
        StudentClass studentClass = studentClassRepository.findById(dto.getStudentClassId()).orElseThrow(() -> new RuntimeException("Class details not found"));
        entity.setName(dto.getName());
        entity.setSex(dto.getSex());
        entity.setSchoolDetails(school);
        entity.setStudentClass(studentClass);
        entity.setRoll(generateRoll(studentClass, vigyanKendraDetails));
        entity.setNumber("NA");
        entity.setVigyanKendraDetails(vigyanKendraDetails);
        return StudentDetailsMapper.studentDetailsMapper.mapFromEntity(studentRepository.save(entity));
    }

    public void delete(Long id) {
        this.enrollmentSessionService.validateAndGetEnrollmentSessionForModification();
        Student entity = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        this.validateVigyanKendraUserPermission(entity);
        studentRepository.deleteById(id);
    }


    private String generateRoll(StudentClass studentClass, VigyanKendraDetails vigyanKendraDetails) {
        return vigyanKendraDetails.getCode() + "/" + studentClass.getName();
    }

    //will be needed in batch processing
    private String generateNumber(StudentClass studentClass) {
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

    public EnrollmentCountResponse countAllEnrollments() {
        return EnrollmentCountResponse.builder().build();
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

    public Map<String, List<StatisticsReportDto>> countByExamCenter() {
        Map<String, List<StatisticsReportDto>> count = new HashMap<>();
        List<VigyanKendraDetails> vigyanKendraDetailsList = vigyanKendraRepository.findAll();
        List<StudentClass> studentClassList = studentClassRepository.findAll();
        vigyanKendraDetailsList.forEach(vigyanKendraDetails -> {
            var counts = examinationCentreDetailsService.getByVigyanKendra(vigyanKendraDetails).stream().map(examinationCentreDetails -> {
                List<StatisticsReportDto> reportDtoList = new ArrayList<>();
                studentClassList.forEach(studentClass -> {
                    var reportDto = new StatisticsReportDto();
                    reportDto.setVigyanKendraCode(vigyanKendraDetails.getCode());
                    reportDto.setClassName(studentClass.getName());
                    reportDto.setExamCenterName(examinationCentreDetails.getName());
                    reportDto.setCount(studentRepository.getCountByStudentClassAndExamCenter(studentClass, examinationCentreDetails));
                    reportDtoList.add(reportDto);
                });
                return reportDtoList;
            }).flatMap(Collection::stream).sorted(Comparator.comparing(StatisticsReportDto::getExamCenterName)).collect(Collectors.toList());
            count.put(vigyanKendraDetails.getCode(), counts);
        });
        return count;
    }

    public void assignRollNumber(Long classId) {

    }

    private void assignRollNumberAsynchronously(Long classId) {
        EnrollmentSession enrollmentSession = enrollmentSessionService.validateAndGetEnrollmentSessionForCreate();
        StudentClass studentClass = studentClassRepository.findById(classId).orElseThrow(() -> new RuntimeException("Class details not found"));
        List<Student> studentList = studentRepository.findByClassAndEnrollmentSession(studentClass, enrollmentSession);
        AtomicInteger number = new AtomicInteger();
        studentList = studentList.stream().sorted(Comparator.comparing((Student student) -> student.getVigyanKendraDetails().getCode()).thenComparing(Student::getName)).map(student -> {
            student.setNumber(String.format("%04d", number.incrementAndGet()));
            return student;
        }).collect(Collectors.toUnmodifiableList());
        studentRepository.saveAll(studentList);
    }

    public List<StudentResponseDto> promoteStudents(Set<Long> studentIds) {
        List<StudentClass> classList = studentClassRepository.findAll();
        if (studentIds.isEmpty()) return Collections.emptyList();
        var currentSession = this.enrollmentSessionService.validateAndGetEnrollmentSessionForModification();
        List<Student> studentList = new ArrayList<>();
        for (Long id : studentIds) {
            Optional<Student> studentOptional = studentRepository.findById(id);
            if (studentOptional.isPresent()) {
                var student = studentOptional.get();
                var studentClass = student.getStudentClass();
                int studentClassWeight = weightedClass.get(studentClass.getName());
                if (studentClassWeight < 10) {
                    var newStudentClass = resolveClass(studentClassWeight + 1, classList);
                    if (Objects.nonNull(newStudentClass)) {
                        student.setStudentClass(newStudentClass);
                        student.setRoll(generateRoll(newStudentClass, student.getVigyanKendraDetails()));
                        student.setEnrollmentSession(currentSession);
                        studentList.add(student);
                    }
                }
            }
        }
        return studentRepository.saveAll(studentList).stream().map(StudentDetailsMapper.studentDetailsMapper::mapFromEntity).collect(Collectors.toUnmodifiableList());
    }

    private StudentClass resolveClass(int classWeight, List<StudentClass> classList) {
        String className = reverseWeightedClass.get(classWeight);
        var studentClassOptional = classList.stream().filter(classDetails -> className.equalsIgnoreCase(classDetails.getName())).findFirst();
        if (studentClassOptional.isPresent()) return studentClassOptional.get();
        return null;
    }
}

