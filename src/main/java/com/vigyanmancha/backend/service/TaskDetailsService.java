package com.vigyanmancha.backend.service;

import com.vigyanmancha.backend.domain.postgres.TaskDetails;
import com.vigyanmancha.backend.domain.postgres.VigyanKendraDetails;
import com.vigyanmancha.backend.repository.postgres.TaskDetailsRepository;
import com.vigyanmancha.backend.utility.DateUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskDetailsService {
    private final TaskDetailsRepository taskDetailsRepository;

    public TaskDetails createTask(String name,
                                  int enrollmentYear,
                                  VigyanKendraDetails vigyanKendraDetails) {
        var taskDetails = new TaskDetails();
        taskDetails.setDate(DateUtility.getCurrentTime());
        taskDetails.setName(name);
        taskDetails.setEnrollmentYear(enrollmentYear);
        taskDetails.setStatus("RUNNING");
        if (Objects.nonNull(vigyanKendraDetails)) {
            taskDetails.setVigyanKendraCode(vigyanKendraDetails.getCode());
            taskDetails.setVigyanKendraName(vigyanKendraDetails.getName());
        }

        return taskDetailsRepository.save(taskDetails);
    }

    public TaskDetails updateSuccessStatus(TaskDetails taskDetails) {
        taskDetails.setStatus("COMPLETED");
        return taskDetailsRepository.save(taskDetails);
    }

    public TaskDetails updateFailedStatus(TaskDetails taskDetails) {
        taskDetails.setStatus("FAILED");
        return taskDetailsRepository.save(taskDetails);
    }

    public List<TaskDetails> getAllTasks() {
        return taskDetailsRepository.findAll();
    }
}
