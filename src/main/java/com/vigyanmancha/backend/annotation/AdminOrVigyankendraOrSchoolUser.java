package com.vigyanmancha.backend.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_VIGYANKENDRA') or hasRole('ROLE_SCHOOL')")
public @interface AdminOrVigyankendraOrSchoolUser {
}
