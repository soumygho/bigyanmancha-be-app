package com.vigyanmancha.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
public class EnrollmentCountResponse {
    private Map<String,Long> counts = new HashMap<>();
    private Long totalCount = 0L;
}
