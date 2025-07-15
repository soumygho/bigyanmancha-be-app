package com.vigyanmancha.backend.migrations;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public class ApplicationContextHolder {
    private static ApplicationContext applicationContext;

    public static synchronized void setApplicationContext(@NotNull ApplicationContext ctx) {
        log.info("Application context initialized");
        ApplicationContextHolder.applicationContext = ctx;
    }
    public synchronized static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.applicationContext;
    }
}
