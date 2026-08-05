package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.ReportDetails;
import com.vigyanmancha.backend.dto.reporting.StatisticsReportDto;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.EnrollmentReportingResponse;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.enums.ReportType;
import com.vigyanmancha.backend.repository.postgres.ReportDetailsRepository;
import com.vigyanmancha.backend.utility.DateUtility;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportingService {
    private final StudentService studentService;
    private final ExcelStorageService excelStorageService;
    private final ReportDetailsRepository reportDetailsRepository;
    private final VigyanKendraDetailsService vigyanKendraDetailsService;
    @Qualifier("reportExecutor")
    private final ExecutorService reportExecutor;
    private final EnrollmentSessionService enrollmentSessionService;
    private final String[] labelsHeadersForEnrollment = {
            "SL. NO.",
            "Vigyan Kendra Name",
            "Vigyan Kendra Code",
            "Exam Centre Name",
            "School Name",
            "Examinee Name",
            "Class",
            "Sex",
            "Roll",
            "No"
    };
    private final String[] labelsHeadersForEnrollmentStatistics = {
            "SL. NO.",
            "Exam Centre Name",
            "Class",
            "Total Count"
    };
    private final List<String> headersForEnrollment = new LinkedList<>(Arrays.asList(labelsHeadersForEnrollment));
    private final List<String> headersForStatistics = new LinkedList<>(Arrays.asList(labelsHeadersForEnrollmentStatistics));
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_COMPLETED = "COMPLETED";

    public List<EnrollmentReportingResponse> getAllReports() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        if (RoleUtility.isVigyanKendraUser()) {
            return reportDetailsRepository.findAll()
                    .stream()
                    .filter(report ->
                            report.getVigyanKendraCode().equals(RoleUtility.getVigyanKendraCode()) &&
                                    STATUS_COMPLETED.equals(report.getStatus()))
                    .map(this::mapFrom)
                    .sorted(Comparator.comparing(r -> LocalDateTime.parse(r.getReportDate(), formatter),
                            Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }
        return reportDetailsRepository.findAll()
                .stream()
                .map(this::mapFrom)
                .sorted(Comparator.comparing(r -> LocalDateTime.parse(r.getReportDate(), formatter),
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public void delete(Long reportId) {
        var reportDetails = reportDetailsRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("No report found with id " + reportId));
        try {
            excelStorageService.deleteExcel(reportDetails.getReportKey());
            reportDetailsRepository.delete(reportDetails);
        } catch (NoSuchKeyException noSuchKeyException) {
            reportDetailsRepository.delete(reportDetails);
        } catch (Exception e) {
            log.error("Unable to delete report with key {} {}", reportDetails.getReportKey(), e.getMessage());
            throw new RuntimeException("Unable to delete report.");
        }
    }

    public EnrollmentReportingResponse generateReportForVigyanKendra(Long vigyanKendraId,
                                                                     long enrollmentId,
                                                                     ReportType reportType) {
        var reportDetails = new ReportDetails();
        reportDetails.setReportKey(UUID.randomUUID().toString());
        reportDetails.setReportDate(DateUtility.getCurrentTime());
        reportDetails.setStatus(STATUS_PENDING);
        reportDetails.setEnrollmentYear(enrollmentSessionService.getEnrollmentSession(enrollmentId).getYear());
        ReportDetails savedReportDetails;
        if(ReportType.ENROLLMENT == reportType) {
            var vigyanKendra =
                    vigyanKendraDetailsService.getVigyanKendraById(vigyanKendraId);
            reportDetails.setVigyanKendraCode(vigyanKendra.getCode());
            reportDetails.setVigyanKendraName(vigyanKendra.getName());
            reportDetails.setReportName("Enrollment Report");
            savedReportDetails = reportDetailsRepository.save(reportDetails);
            reportExecutor.submit(() -> {
                processEnrollmentReportRequest(savedReportDetails, vigyanKendra);
            });
            return mapFrom(savedReportDetails);
        } else {
            reportDetails.setReportName("Statistics Report");
            savedReportDetails = reportDetailsRepository.save(reportDetails);
            reportExecutor.submit(() -> {
                processStaticsReportRequest(reportDetails);
            });
            return mapFrom(savedReportDetails);
        }
    }

    private void processStaticsReportRequest(ReportDetails reportDetails) {
        try {
            reportDetails.setStatus(STATUS_RUNNING);
            reportDetailsRepository.save(reportDetails);
            Map<String, List<StatisticsReportDto>> counts = studentService.countByExamCenter();
            var bytes = prepareExcelForStaticsReport(counts);
            excelStorageService.uploadExcel(reportDetails.getReportKey(), bytes);
            reportDetails.setStatus(STATUS_COMPLETED);
        } catch (Exception e) {
            reportDetails.setStatus(STATUS_FAILED);
            log.error(e.getMessage());
        }
        reportDetailsRepository.save(reportDetails);
    }



    private void processEnrollmentReportRequest(ReportDetails reportDetails,
                                                VigyanKendraDetailsRequestDTO vigyanKendra) {
        reportDetails.setStatus(STATUS_RUNNING);
        reportDetailsRepository.save(reportDetails);
        List<StudentResponseDto> studentResponseDtoList
                = studentService.getAllByVigyanKendraId(vigyanKendra.getId());
        try {
            var bytes = prepareExcel(studentResponseDtoList, vigyanKendra);
            excelStorageService.uploadExcel(reportDetails.getReportKey(), bytes);
            reportDetails.setStatus(STATUS_COMPLETED);
        } catch (Exception e) {
            reportDetails.setStatus(STATUS_FAILED);
            log.error(e.getMessage());
        }
        reportDetailsRepository.save(reportDetails);
    }

    private byte[] prepareExcel(List<StudentResponseDto> enrollmentList,
                                VigyanKendraDetailsRequestDTO vigyanKendraDetails) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet(vigyanKendraDetails.getName());
            var row = sheet.createRow(0);
            for (int i = 0; i < headersForEnrollment.size(); i++) {
                row.createCell(i).setCellValue(headersForEnrollment.get(i));
            }
            for (int i = 0; i < enrollmentList.size(); i++) {
                var dataRow = sheet.createRow(i + 1);
                var enrollment = enrollmentList.get(i);
                dataRow.createCell(0).setCellValue(i + 1);
                dataRow.createCell(1).setCellValue(vigyanKendraDetails.getName());
                dataRow.createCell(2).setCellValue(vigyanKendraDetails.getCode());
                dataRow.createCell(3).setCellValue(
                        Objects.nonNull(enrollment.getExaminationCentreName()) ? enrollment.getExaminationCentreName() : "");
                dataRow.createCell(4).setCellValue(enrollment.getSchoolName());
                dataRow.createCell(5).setCellValue(enrollment.getName());
                dataRow.createCell(6).setCellValue(enrollment.getClassName());
                dataRow.createCell(7).setCellValue(enrollment.getSex());
                dataRow.createCell(8).setCellValue(enrollment.getRoll());
                dataRow.createCell(9).setCellValue(enrollment.getNumber());
            }
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            log.error("Error while preparing excel {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private byte[] prepareExcelForStaticsReport(Map<String, List<StatisticsReportDto>> counts) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            counts.entrySet().forEach(e -> {
                var sheet = workbook.createSheet(e.getKey());
                var row = sheet.createRow(0);
                for (int i = 0; i < headersForStatistics.size(); i++) {
                    row.createCell(i).setCellValue(headersForStatistics.get(i));
                }
                var countList = e.getValue();
                for (int i = 0; i < countList.size(); i++) {
                    var countDetails = countList.get(i);
                    var dataRow = sheet.createRow(i + 1);
                    dataRow.createCell(0).setCellValue(i + 1);
                    dataRow.createCell(1).setCellValue(countDetails.getExamCenterName());
                    dataRow.createCell(2).setCellValue(countDetails.getClassName());
                    dataRow.createCell(3).setCellValue(countDetails.getCount());
                }
            });
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            log.error("Error while preparing excel {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private EnrollmentReportingResponse mapFrom(ReportDetails reportDetails) {
        var response = new EnrollmentReportingResponse();
        response.setId(reportDetails.getId());
        response.setReportKey(reportDetails.getReportKey());
        response.setReportDate(reportDetails.getReportDate());
        response.setEnrollmentYear(reportDetails.getEnrollmentYear());
        response.setVigyanKendraCode(reportDetails.getVigyanKendraCode());
        response.setVigyanKendraName(reportDetails.getVigyanKendraName());
        response.setReportName(reportDetails.getReportName());
        response.setStatus(reportDetails.getStatus());
        return response;
    }

    @PreDestroy
    public void destroy() {
        reportExecutor.shutdown();
        try {
            if (!reportExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                reportExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            reportExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Autowired
    public ReportingService(final StudentService studentService,
                            final ExcelStorageService excelStorageService,
                            final ReportDetailsRepository reportDetailsRepository,
                            final VigyanKendraDetailsService vigyanKendraDetailsService,
                            @Qualifier("reportExecutor") final ExecutorService reportExecutor,
                            final EnrollmentSessionService enrollmentSessionService) {
        this.studentService = studentService;
        this.excelStorageService = excelStorageService;
        this.reportDetailsRepository = reportDetailsRepository;
        this.vigyanKendraDetailsService = vigyanKendraDetailsService;
        this.reportExecutor = reportExecutor;
        this.enrollmentSessionService = enrollmentSessionService;
    }

}
