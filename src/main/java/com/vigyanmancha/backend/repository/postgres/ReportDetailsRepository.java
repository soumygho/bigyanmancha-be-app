package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.ReportDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDetailsRepository extends JpaRepository<ReportDetails, Long> {
}
