package com.vigyanmancha.backend.controller;

import com.vigyanmancha.backend.annotation.AdminUser;
import com.vigyanmancha.backend.domain.postgres.Role;
import com.vigyanmancha.backend.domain.postgres.UserDetails;
import com.vigyanmancha.backend.dto.response.SignupRequest;
import com.vigyanmancha.backend.dto.response.UserDetailsResponseDto;
import com.vigyanmancha.backend.enums.ERole;
import com.vigyanmancha.backend.repository.postgres.RoleRepository;
import com.vigyanmancha.backend.repository.postgres.UserDetailsRepository;
import com.vigyanmancha.backend.service.VigyanKendraDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "User management api")
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final VigyanKendraDetailsService vigyanKendraDetailsService;

    private UserDetailsResponseDto toDto(UserDetails user) {
        UserDetailsResponseDto dto = new UserDetailsResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUserName());
        dto.setEmail(user.getEmail());
        var roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        dto.setRoles(roles);
        var isAdmin = roles.contains("ROLE_ADMIN");
        dto.setAdmin(isAdmin);
        var vigyanKendra = user.getVigyanKendraDetails();
        if(Objects.nonNull(vigyanKendra)) {
            dto.setVigyanKendraId(vigyanKendra.getId());
            dto.setVigyanKendraCode(vigyanKendra.getCode());
            dto.setVigyanKendraName(vigyanKendra.getName());
        }
        return dto;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public UserDetailsResponseDto registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        // Create new user's account
        var user = new UserDetails();
        user.setUserName(signUpRequest.getUserName());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setRoles(validateAndResolveRoles(signUpRequest.getRoles()));
        if(!signUpRequest.isAdmin()) {
            user.setVigyanKendraDetails(vigyanKendraDetailsService.existOrThrow(signUpRequest.getVigyanKendraId()));
        }
        userRepository.save(user);
        return toDto(user);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @AdminUser
    public UserDetailsResponseDto getUser(@PathVariable Long id) {
        var user =  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(user);
    }

    @PutMapping(produces = "application/json", consumes = "application/json")
    @AdminUser
    public UserDetailsResponseDto updateUser(@Valid @RequestBody SignupRequest signUpRequest) {
        var user =  userRepository.findById(signUpRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserName(signUpRequest.getUserName());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setRoles(validateAndResolveRoles(signUpRequest.getRoles()));
        if(!signUpRequest.isAdmin()) {
            user.setVigyanKendraDetails(vigyanKendraDetailsService.existOrThrow(signUpRequest.getVigyanKendraId()));
        }
        var userEntity = userRepository.save(user);
        return toDto(userEntity);
    }

    @DeleteMapping("/{id}")
    @AdminUser
    public void deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @GetMapping(produces = "application/json")
    public List<UserDetailsResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private Set<Role> validateAndResolveRoles(Set<String> strRoles) {
        var roles = new HashSet<Role>();

        if (strRoles == null) {
            var userRole = roleRepository.findByName(ERole.ROLE_NONE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin" -> {
                        var adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "vigyankendra" -> {
                        var modRole = roleRepository.findByName(ERole.ROLE_VIGYANKENDRA)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }

                    case "school" -> {
                        var modRole = roleRepository.findByName(ERole.ROLE_SCHOOL)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        var userRole = roleRepository.findByName(ERole.ROLE_NONE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        return roles;
    }
}
