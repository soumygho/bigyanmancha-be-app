package com.vigyanmancha.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentDetails {
    private String name;
    private String roll;
    private String no;
}
