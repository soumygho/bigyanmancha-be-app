package com.vigyanmancha.backend.config.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    public static String SYSTEM = "system";

    @Override
    public Optional<String> getCurrentAuditor() {
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        return Optional.of(authentication.getName());*/
        return Optional.of(SYSTEM);
    }
}
