package com.iudmarket.iudmarket.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Pool de hilos para cobros concurrentes.
 * El nombre del bean debe coincidir con @Async("cajeraExecutor") en CompraService.
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "cajeraExecutor")
    public Executor cajeraExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("Cajera-");
        executor.initialize();
        return executor;
    }
}
