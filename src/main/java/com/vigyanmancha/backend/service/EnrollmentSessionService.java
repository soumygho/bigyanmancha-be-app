package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.EnrollmentSession;
import com.vigyanmancha.backend.repository.postgres.EnrollmentSessionRepository;
import com.vigyanmancha.backend.utility.auth.RoleUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentSessionService {
    private final EnrollmentSessionRepository enrollmentSessionRepository;

    public List<EnrollmentSession> getEnrollmentSessions() {
        return enrollmentSessionRepository.findAll();
    }

    public EnrollmentSession getEnrollmentSession(long id) {
        return enrollmentSessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No enrollmentSession with id " + id));
    }

    public EnrollmentSession createEnrollmentSession(EnrollmentSession enrollmentSession) {
        enrollmentSession.setEnrollmentFreezed(true);
        enrollmentSession.setModificationFreezed(true);
        enrollmentSession.setActive(false);
        return enrollmentSessionRepository.save(enrollmentSession);
    }

    public EnrollmentSession updateEnrollmentSession(EnrollmentSession enrollmentSession) {
        var existing = getEnrollmentSession(enrollmentSession.getId());
        if (!existing.isActive()) {
            throw new RuntimeException("Can't modify an inactive enrollmentSession");
        }
        existing.setName(enrollmentSession.getName());
        existing.setYear(enrollmentSession.getYear());
        return enrollmentSessionRepository.save(existing);
    }

    public void deleteEnrollmentSession(long id) {
        var existing = getEnrollmentSession(id);
        if (existing.isActive()) {
            throw new RuntimeException("Can't delete an active enrollmentSession");
        }
        enrollmentSessionRepository.delete(existing);
    }

    public EnrollmentSession setAsActive(long id) {
        var activeEnrollmentSessions = enrollmentSessionRepository.findByActive(true);
        if (!activeEnrollmentSessions.isEmpty()) {
            throw new RuntimeException("There should be only one active enrollmentSession at any given time");
        }
        EnrollmentSession existing = getEnrollmentSession(id);
        existing.setActive(true);
        return enrollmentSessionRepository.save(existing);
    }

    public EnrollmentSession setAsInActive(long id) {
        EnrollmentSession existing = getEnrollmentSession(id);
        existing.setActive(false);
        return enrollmentSessionRepository.save(existing);
    }

    public EnrollmentSession setEnrollmentFreezed(long id) {
        EnrollmentSession existing = getEnrollmentSession(id);
        if(!existing.isActive()) {
            throw new RuntimeException("Can't modify an inactive enrollmentSession");
        }
        existing.setEnrollmentFreezed(true);
        return enrollmentSessionRepository.save(existing);
    }

    public EnrollmentSession setEnrollmentUnFreezed(long id) {
        EnrollmentSession existing = getEnrollmentSession(id);
        if(!existing.isActive()) {
            throw new RuntimeException("Can't modify an inactive enrollmentSession");
        }
        existing.setEnrollmentFreezed(false);
        return enrollmentSessionRepository.save(existing);
    }

    public EnrollmentSession setModificationFreezed(long id) {
        EnrollmentSession existing = getEnrollmentSession(id);
        if(!existing.isActive()) {
            throw new RuntimeException("Can't modify an inactive enrollmentSession");
        }
        existing.setModificationFreezed(true);
        return enrollmentSessionRepository.save(existing);
    }

    public EnrollmentSession setModificationUnFreezed(long id) {
        EnrollmentSession existing = getEnrollmentSession(id);
        if(!existing.isActive()) {
            throw new RuntimeException("Can't modify an inactive enrollmentSession");
        }
        existing.setModificationFreezed(false);
        return enrollmentSessionRepository.save(existing);
    }

    public EnrollmentSession validateAndGetEnrollmentSessionForCreate() {
        var activeEnrollmentSessions = enrollmentSessionRepository.findByActive(true);
        if (activeEnrollmentSessions.isEmpty()) {
            throw new RuntimeException("No active enrollmentSession found");
        }
        if (activeEnrollmentSessions.size() > 1) {
            throw new RuntimeException("More than one active enrollmentSession found");
        }
        var session = activeEnrollmentSessions.get(0);
        if (RoleUtility.isVigyanKendraUser()) {
            if (session.isEnrollmentFreezed()) throw new RuntimeException("EnrollmentSession is already freezed");
        } else if (RoleUtility.isAdminUser()) {
            if(!session.isActive()) throw new RuntimeException("EnrollmentSession is not active");
        }
        return session;
    }

    public EnrollmentSession validateAndGetEnrollmentSessionForModification() {
        var activeEnrollmentSessions = enrollmentSessionRepository.findByActive(true);
        if (activeEnrollmentSessions.isEmpty()) {
            throw new RuntimeException("No active enrollmentSession found");
        }
        if (activeEnrollmentSessions.size() > 1) {
            throw new RuntimeException("More than one active enrollmentSession found");
        }
        var session = activeEnrollmentSessions.get(0);
        if (RoleUtility.isVigyanKendraUser()) {
            if (session.isModificationFreezed()) throw new RuntimeException("EnrollmentSession is already freezed for modification");
        } else if (RoleUtility.isAdminUser()) {
            if(!session.isActive()) throw new RuntimeException("EnrollmentSession is not active");
        }
        return session;
    }
}
