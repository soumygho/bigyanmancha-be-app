package com.vigyanmancha.backend.utility;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PasswordChecker {
    // Pattern to match BCrypt encoded passwords
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    /**
     * Checks if a given string looks like a BCrypt encoded password.
     *
     * @param password The string to check.
     * @return true if the string matches the BCrypt pattern, false otherwise.
     */
    public static boolean isBcryptEncoded(String password) {
        if (password == null) {
            return false;
        }
        return BCRYPT_PATTERN.matcher(password).matches();
    }
}
