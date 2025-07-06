package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.SubjectDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectDetailsRepository extends JpaRepository<SubjectDetails, Long> {}
