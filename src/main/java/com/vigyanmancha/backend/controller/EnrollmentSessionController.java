package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminOrVigyankendraUser;
import com.vigyanmancha.backend.annotation.AdminUser;
import com.vigyanmancha.backend.domain.postgres.EnrollmentSession;
import com.vigyanmancha.backend.service.EnrollmentSessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollment session management api")
@RestController
@RequestMapping("/api/enrollment-session")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
@Slf4j
public class EnrollmentSessionController {
    private final EnrollmentSessionService enrollmentSessionService;

    @GetMapping(produces = "application/json")
    @AdminOrVigyankendraUser
    public List<EnrollmentSession> getAllEnrollmentSession() {
        return enrollmentSessionService.getEnrollmentSessions();
    }

    @GetMapping(path="/{id}",produces = "application/json")
    @AdminOrVigyankendraUser
    public EnrollmentSession getEnrollmentSessionById(@PathVariable Long id) {
        return enrollmentSessionService.getEnrollmentSession(id);
    }

    @DeleteMapping(path="/{id}", produces = "application/json")
    @AdminUser
    public void deleteEnrollmentSessionById(@PathVariable Long id) {
        enrollmentSessionService.deleteEnrollmentSession(id);
    }

    @PostMapping(produces = "application/json")
    @AdminUser
    public EnrollmentSession  createEnrollmentSession(@RequestBody EnrollmentSession enrollmentSession) {
        return enrollmentSessionService.createEnrollmentSession(enrollmentSession);
    }

    @PutMapping(produces = "application/json")
    @AdminUser
    public EnrollmentSession  updateEnrollmentSession(@RequestBody EnrollmentSession enrollmentSession) {
        return enrollmentSessionService.updateEnrollmentSession(enrollmentSession);
    }

    @GetMapping(path="/{id}/active",produces = "application/json")
    public EnrollmentSession setEnrollmentSessionAsActive(@PathVariable Long id) {
        return enrollmentSessionService.setAsActive(id);
    }

    @GetMapping(path="/{id}/inactive",produces = "application/json")
    public EnrollmentSession setEnrollmentSessionAsInActive(@PathVariable Long id) {
        return enrollmentSessionService.setAsInActive(id);
    }
    @GetMapping(path="/{id}/freeze",produces = "application/json")
    public EnrollmentSession setEnrollmentFreezed(@PathVariable Long id) {
        return enrollmentSessionService.setEnrollmentFreezed(id);
    }
    @GetMapping(path="/{id}/unfreeze",produces = "application/json")
    public EnrollmentSession setEnrollmentUnFreezed(@PathVariable Long id) {
        return enrollmentSessionService.setEnrollmentUnFreezed(id);
    }
    @GetMapping(path="/{id}/modification-freeze",produces = "application/json")
    EnrollmentSession setModificationFreezed(@PathVariable long id) {
        return enrollmentSessionService.setModificationFreezed(id);
    }
    @GetMapping(path="/{id}/modification-unfreeze",produces = "application/json")
    EnrollmentSession setModificationUnFreezed(@PathVariable long id) {
        return enrollmentSessionService.setModificationUnFreezed(id);
    }
}
