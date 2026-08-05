package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.ExaminationCentreDetails;
import com.vigyanmancha.backend.domain.postgres.VigyanKendraDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExaminationCentreDetailsRepository extends JpaRepository<ExaminationCentreDetails, Long> {
    @Query("SELECT e FROM ExaminationCentreDetails e WHERE e.vigyanKendraDetails = :vigyankendradetails")
    List<ExaminationCentreDetails> getByVigyanKendra(@Param("vigyankendradetails") VigyanKendraDetails vigyanKendraDetails);
}

