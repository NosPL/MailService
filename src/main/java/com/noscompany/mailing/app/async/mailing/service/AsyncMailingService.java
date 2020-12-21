package com.noscompany.mailing.app.async.mailing.service;


import com.noscompany.mailing.app.async.mailing.properties.Timeout;
import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.synchronous.mailing.MailService;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Log4j2
public class AsyncMailingService {
    private ThreadPoolExecutor threadPool;
    @Setter
    private MailServiceCreator mailServiceCreator;
    @Getter
//   for testing
    private int submitInvocationCount = 0;
    @Setter
    @Getter
    private Timeout timeout;

    public AsyncMailingService(ThreadPoolExecutor threadPool, MailServiceCreator mailServiceCreator, Timeout timeout) {
        this.threadPool = threadPool;
        this.mailServiceCreator = mailServiceCreator;
        this.timeout = timeout;
    }

    public Option<MailingFuture> sendAsync(EmailDto dto) {
        this.submitInvocationCount++;
        try {
            return Option.of(futureFrom(dto));
        } catch (RejectedExecutionException e) {
            log.warn("rejected async mailing request, cause: ", e);
            return Option.none();
        }
    }

    private MailingFuture futureFrom(EmailDto dto) {
        CompletableFuture<MailService.MailingResult> cf = completableFuture(dto);
        return new MailingFuture(cf, timeout);
    }

    private CompletableFuture<MailService.MailingResult> completableFuture(EmailDto dto) {
        MailService mailService = mailServiceCreator.getNewInstance();
        return supplyAsync(
                () -> mailService.send(dto),
                threadPool);
    }

    public int workingThreadsCount() {
        return threadPool.getActiveCount();
    }

    public int threadPoolMaxSize() {
        return threadPool.getMaximumPoolSize();
    }

    public boolean isEmpty() {
        return workingThreadsCount() == 0;
    }

    public boolean isFull() {
        return threadPool.getMaximumPoolSize() == threadPool.getActiveCount();
    }
}