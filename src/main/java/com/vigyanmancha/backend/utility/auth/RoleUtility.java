package com.vigyanmancha.backend.utility.auth;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class RoleUtility {
    public boolean isAdminUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = getRoles(authentication);
        return roles.contains("ROLE_ADMIN");
    }

    public boolean isVigyanKendraUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = getRoles(authentication);
        return roles.contains("ROLE_VIGYANKENDRA");
    }

    public boolean isSchoolUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = getRoles(authentication);
        return roles.contains("ROLE_SCHOOL");
    }

    public Long getVigyanKendraId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> claims = (Map<String, Object>) authentication.getDetails();
        log.info("Claims: {}", claims);
        return Objects.nonNull(claims.get("vigyanKendraId")) ? ((Integer) claims.get("vigyanKendraId")).longValue() : 0;
    }

    public String getVigyanKendraCode() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> claims = (Map<String, Object>) authentication.getDetails();
        log.info("Claims: {}", claims);
        return Objects.nonNull(claims.get("vigyanKendraCode")) ? (String) claims.get("vigyanKendraCode") : "";
    }

    private Set<String> getRoles(Authentication authentication) {
        if (Objects.isNull(authentication)) {
            return Collections.emptySet();
        }
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
