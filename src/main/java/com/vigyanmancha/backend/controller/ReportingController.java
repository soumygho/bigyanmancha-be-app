package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminOrVigyankendraUser;
import com.vigyanmancha.backend.annotation.AdminUser;
import com.vigyanmancha.backend.dto.request.EnrollmentReportingRequest;
import com.vigyanmancha.backend.dto.response.EnrollmentReportingResponse;
import com.vigyanmancha.backend.service.ExcelStorageService;
import com.vigyanmancha.backend.service.ReportingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reporting")
@Tag(name = "Reporting api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
public class ReportingController {
    private final ExcelStorageService excelStorageService;
    private final ReportingService reportingService;

    @PostMapping(path = "/enrollment",
            consumes = "application/json",
            produces = "application/json")
    @AdminOrVigyankendraUser
    public EnrollmentReportingResponse prepareReport(
            @RequestBody EnrollmentReportingRequest enrollmentReportingRequest) {
        return reportingService.generateReportForVigyanKendra(enrollmentReportingRequest.getVigyanKendraId(),
                enrollmentReportingRequest.getEnrollmentSessionId());
    }

    @GetMapping(path = "/enrollment", produces = "application/json")
    @AdminOrVigyankendraUser
    public List<EnrollmentReportingResponse> getAllReports() {
        return reportingService.getAllReports();
    }

    @DeleteMapping(path = "/enrollment/{id}")
    @AdminUser
    public void deleteReport(@PathVariable("id") long id) {
        reportingService.delete(id);
    }

    @GetMapping(value = "/download/{vigyanKendraCode}/{key}", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable String vigyanKendraCode, @PathVariable String key) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + vigyanKendraCode + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelStorageService.downloadExcel(key));
    }
}
