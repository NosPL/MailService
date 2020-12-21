package com.noscompany.mailing.app.async.mailing;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.service.MailServiceCreator;

import com.noscompany.mailing.app.async.mailing.properties.ThreadPoolSize;
import com.noscompany.mailing.app.async.mailing.properties.Timeout;
import com.noscompany.mailing.app.synchronous.mailing.SyncMailingConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
@Import(SyncMailingConfig.class)
public class AsyncMailConfig {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public AsyncMailingService asyncMailing(MailServiceCreator mailServiceCreator, ThreadPoolSize threadPoolSize) {
        return new AsyncMailingService(
                threadPool(threadPoolSize),
                mailServiceCreator,
                Timeout.of(20, SECONDS));
    }

    private ThreadPoolExecutor threadPool(ThreadPoolSize threadPoolSize) {
        int threadPoolSizeValue = Math.min(threadPoolSize.getValue(), 20);
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("async-mailing-service-thread-%d")
                .build();
        return new ThreadPoolExecutor(
                threadPoolSizeValue,
                threadPoolSizeValue,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                threadFactory,
                new AbortPolicy());
    }

    @Bean
    public ThreadPoolSize threadPoolSize() {
        return ThreadPoolSize.of(10);
    }
}