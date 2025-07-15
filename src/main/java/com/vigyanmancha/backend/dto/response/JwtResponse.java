package com.vigyanmancha.backend.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class JwtResponse {
    private String jwt;
    private Long id;
    private String userName;
    private Set<String> roles;
    private Long vigyanKendraId;
    private String vigyanKendraName;
    private String vigyanKendraCode;
    private boolean isAdminUser;
    private boolean isVigyanKendraUser;
    private boolean isSchoolUser;
}
