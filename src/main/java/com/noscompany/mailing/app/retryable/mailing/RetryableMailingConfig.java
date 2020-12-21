package com.noscompany.mailing.app.retryable.mailing;

import com.noscompany.mailing.app.async.mailing.AsyncMailConfig;
import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.retryable.mailing.entity.EmailRepo;
import com.noscompany.mailing.app.retryable.mailing.properties.AttemptLimit;
import com.noscompany.mailing.app.retryable.mailing.properties.BatchSize;
import com.noscompany.mailing.app.retryable.mailing.service.RetryableMailingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.time.Clock;

@Configuration
@Import(AsyncMailConfig.class)
@EnableJpaRepositories(basePackages = "com.noscompany.mailing.app.retryable.mailing.entity")
public class RetryableMailingConfig {

    @Bean
    DataSource datasource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    public RetryableMailingService scheduledSending(EmailRepo emailRepo, AsyncMailingService asyncMailingService) {
        return new RetryableMailingService(
                emailRepo,
                asyncMailingService,
                BatchSize.of(10),
                AttemptLimit.of(3),
                clock());
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}