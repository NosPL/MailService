package com.noscompany.mailing.app.scheduling;

import com.noscompany.mailing.app.retryable.mailing.service.RetryableMailingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Bean
    ScheduledMailing scheduledActions(RetryableMailingService service) {
        return new ScheduledMailing(service);
    }
}
