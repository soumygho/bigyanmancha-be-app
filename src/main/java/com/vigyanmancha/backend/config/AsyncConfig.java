package com.vigyanmancha.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class AsyncConfig {
    @Bean(name = "reportExecutor")
    public ExecutorService reportExecutorService() {
        return new ThreadPoolExecutor(
                1,                   // core pool size
                1,                   // max pool size
                30, TimeUnit.SECONDS,// idle thread keep-alive time
                new java.util.concurrent.LinkedBlockingQueue<>(),  // task queue
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()  // Rejection policy
        );
    }
}