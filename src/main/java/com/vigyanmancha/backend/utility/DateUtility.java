package com.vigyanmancha.backend.utility;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtility {
    public String getCurrentTime() {
        // Get current date-time
        LocalDateTime now = LocalDateTime.now();

        // Define the formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        // Format the date-time
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
}
