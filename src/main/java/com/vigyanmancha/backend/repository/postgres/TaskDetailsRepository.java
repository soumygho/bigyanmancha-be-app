package com.vigyanmancha.backend.repository.postgres;

import com.vigyanmancha.backend.domain.postgres.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDetailsRepository extends JpaRepository<TaskDetails, Long> {
}
