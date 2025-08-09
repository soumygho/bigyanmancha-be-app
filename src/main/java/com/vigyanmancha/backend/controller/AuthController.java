package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.domain.postgres.UserDetails;
import com.vigyanmancha.backend.dto.request.LoginRequest;
import com.vigyanmancha.backend.dto.response.JwtResponse;
import com.vigyanmancha.backend.enums.ERole;
import com.vigyanmancha.backend.repository.postgres.RoleRepository;
import com.vigyanmancha.backend.repository.postgres.UserDetailsRepository;
import com.vigyanmancha.backend.service.security.JwtUtils;
import com.vigyanmancha.backend.service.security.UserDetailsImpl;
import com.vigyanmancha.backend.utility.PasswordChecker;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsRepository userDetailsRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final String defaultAdminUserName = "master";
    private final JwtUtils jwtUtils;



    @PostMapping(path="/signin", produces = "application/json", consumes = "application/json")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        encryptDefaultAdminPassword();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        UserDetails userDetailsEntity = userDetailsRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User is not found."));
        var jwtResponse = new JwtResponse();
        jwtResponse.setRoles(roles);
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setUserName(userDetails.getUsername());

        if(roles.contains(ERole.ROLE_ADMIN.name())) {
            jwtResponse.setAdminUser(true);
        } else if(roles.contains(ERole.ROLE_VIGYANKENDRA.name())) {
            jwtResponse.setVigyanKendraUser(true);
        } else if(roles.contains(ERole.ROLE_SCHOOL.name())){
            jwtResponse.setSchoolUser(true);
        }

        if (!roles.isEmpty() && !roles.contains(ERole.ROLE_ADMIN.name())) {
            var vigyanKendraDetails = userDetailsEntity.getVigyanKendraDetails();
            jwtResponse.setVigyanKendraId(vigyanKendraDetails.getId());
            jwtResponse.setVigyanKendraName(vigyanKendraDetails.getName());
            jwtResponse.setVigyanKendraCode(vigyanKendraDetails.getCode());
        }
        String jwt = jwtUtils.generateJwtToken(authentication, jwtResponse);
        jwtResponse.setJwt(jwt);
        return jwtResponse;
    }

    void encryptDefaultAdminPassword() {
        var user = userDetailsRepository.findByUserName(defaultAdminUserName)
                .orElseThrow(() -> new Error("Error: Default User is not found. Please configure it."));
        if(user.getRoles().isEmpty()) {
            var adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new Error("Error: Admin Role is not found. Please configure it."));
            user.getRoles().add(adminRole);
            userDetailsRepository.save(user);
        }

        if (!PasswordChecker.isBcryptEncoded(user.getPassword())) {
            log.warn("Admin password is not encoded now encoding it.");
            user.setPassword(encoder.encode(user.getPassword()));
            userDetailsRepository.save(user);
        }
    }
}
