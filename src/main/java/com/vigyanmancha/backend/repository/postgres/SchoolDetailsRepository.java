package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.SchoolDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchoolDetailsRepository extends JpaRepository<SchoolDetails, Long> {
}
