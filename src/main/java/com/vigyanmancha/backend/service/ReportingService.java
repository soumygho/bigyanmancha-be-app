package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.ReportDetails;
import com.vigyanmancha.backend.dto.request.VigyanKendraDetailsRequestDTO;
import com.vigyanmancha.backend.dto.response.EnrollmentReportingResponse;
import com.vigyanmancha.backend.dto.response.StudentResponseDto;
import com.vigyanmancha.backend.repository.postgres.ReportDetailsRepository;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportingService {
    private final StudentService studentService;
    private final ExcelStorageService excelStorageService;
    private final ReportDetailsRepository reportDetailsRepository;
    private final VigyanKendraDetailsService vigyanKendraDetailsService;
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
    private final List<String> headersForEnrollment = new LinkedList<>(Arrays.asList(labelsHeadersForEnrollment));
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_COMPLETED = "COMPLETED";

    public List<EnrollmentReportingResponse> getAllReports() {
        if (RoleUtility.isVigyanKendraUser()) {
            return reportDetailsRepository.findAll()
                    .stream()
                    .filter(report ->
                            report.getVigyanKendraCode().equals(RoleUtility.getVigyanKendraCode()) &&
                                    STATUS_COMPLETED.equals(report.getStatus()))
                    .map(this::mapFrom)
                    .collect(Collectors.toList());
        }
        return reportDetailsRepository.findAll()
                .stream()
                .map(this::mapFrom)
                .collect(Collectors.toList());
    }

    public void delete(Long reportId) {
        var reportDetails = reportDetailsRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("No report found with id " + reportId));
        try {
            excelStorageService.deleteExcel(reportDetails.getReportKey());
            reportDetailsRepository.delete(reportDetails);
        }
        catch (NoSuchKeyException noSuchKeyException) {
            reportDetailsRepository.delete(reportDetails);
        }
        catch (Exception e) {
            log.error("Unable to delete report with key {} {}", reportDetails.getReportKey(), e.getMessage());
            throw new RuntimeException("Unable to delete report.");
        }
    }

    public EnrollmentReportingResponse generateReportForVigyanKendra(Long vigyanKendraId, long enrollmentId) {
        var vigyanKendra =
                vigyanKendraDetailsService.getVigyanKendraById(vigyanKendraId);
        var reportDetails = new ReportDetails();
        reportDetails.setReportKey(UUID.randomUUID().toString());
        reportDetails.setReportDate(getCurrentTime());
        reportDetails.setVigyanKendraCode(vigyanKendra.getCode());
        reportDetails.setVigyanKendraName(vigyanKendra.getName());
        reportDetails.setStatus(STATUS_PENDING);
        reportDetails.setEnrollmentYear(enrollmentSessionService.getEnrollmentSession(enrollmentId).getYear());
        final var savedReportDetails = reportDetailsRepository.save(reportDetails);
        reportExecutor.submit(() -> {
            processEnrollmentReportRequest(savedReportDetails, vigyanKendra);
        });
        return mapFrom(savedReportDetails);
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

    private String getCurrentTime() {
        // Get current date-time
        LocalDateTime now = LocalDateTime.now();

        // Define the formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Format the date-time
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }

    private EnrollmentReportingResponse mapFrom(ReportDetails reportDetails) {
        var response = new EnrollmentReportingResponse();
        response.setId(reportDetails.getId());
        response.setReportKey(reportDetails.getReportKey());
        response.setReportDate(reportDetails.getReportDate());
        response.setEnrollmentYear(reportDetails.getEnrollmentYear());
        response.setVigyanKendraCode(reportDetails.getVigyanKendraCode());
        response.setVigyanKendraName(reportDetails.getVigyanKendraName());
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

}
