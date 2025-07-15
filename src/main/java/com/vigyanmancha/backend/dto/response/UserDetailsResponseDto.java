package com.vigyanmancha.backend.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class UserDetailsResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private Long vigyanKendraId;
    private String vigyanKendraCode;
    private String vigyanKendraName;
    private boolean isAdmin;
}
