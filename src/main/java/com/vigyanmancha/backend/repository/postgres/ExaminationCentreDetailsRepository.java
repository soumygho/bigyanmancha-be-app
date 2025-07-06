package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.ExaminationCentreDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationCentreDetailsRepository extends JpaRepository<ExaminationCentreDetails, Long> {
}

