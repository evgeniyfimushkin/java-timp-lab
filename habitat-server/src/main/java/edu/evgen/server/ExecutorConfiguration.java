package edu.evgen.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfiguration {

    @Bean("processTaskExecutor")
    TaskExecutor getProcessTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("process");
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        return taskExecutor;
    }
}
