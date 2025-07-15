package com.vigyanmancha.backend.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    private Long userId;
    private String userName;
    private String password;
    private String email;
    private Long vigyanKendraId;
    private Set<String> roles;

    public boolean isAdmin() {
        return roles.contains("admin");
    }
}
