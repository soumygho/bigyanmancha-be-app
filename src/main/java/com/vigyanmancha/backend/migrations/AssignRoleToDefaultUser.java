package com.vigyanmancha.backend.migrations;

import com.vigyanmancha.backend.enums.ERole;
import com.vigyanmancha.backend.repository.postgres.RoleRepository;
import com.vigyanmancha.backend.repository.postgres.UserDetailsRepository;
import com.vigyanmancha.backend.utility.PasswordChecker;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Slf4j
public class AssignRoleToDefaultUser implements CustomTaskChange {
    private UserDetailsRepository userDetailsRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private final String defaultAdminUserName = "master";
    @Override
    public void execute(Database database) throws CustomChangeException {
        encryptDefaultAdminPassword();
        var user = userDetailsRepository.findByUserName(defaultAdminUserName)
                .orElseThrow(() -> new Error("Error: Default User is not found. Please configure it."));
        var adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new Error("Error: Admin Role is not found. Please configure it."));
        user.setRoles(Set.of(adminRole));
        userDetailsRepository.save(user);
    }

    void encryptDefaultAdminPassword() {
        var user = userDetailsRepository.findByUserName(defaultAdminUserName)
                .orElseThrow(() -> new Error("Error: Default User is not found. Please configure it."));
        if (!PasswordChecker.isBcryptEncoded(user.getPassword())) {
            log.warn("Admin password is not encoded now encoding it.");
            user.setPassword(encoder.encode(user.getPassword()));
            userDetailsRepository.save(user);
        }
    }

    @Override
    public String getConfirmationMessage() {
        return "";
    }

    @Override
    public void setUp() throws SetupException {
        userDetailsRepository = ApplicationContextHolder.getApplicationContext().getBean(UserDetailsRepository.class);
        roleRepository = ApplicationContextHolder.getApplicationContext().getBean(RoleRepository.class);
        encoder = ApplicationContextHolder.getApplicationContext().getBean(PasswordEncoder.class);
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }

}
