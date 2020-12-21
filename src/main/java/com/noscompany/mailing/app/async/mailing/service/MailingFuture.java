package com.noscompany.mailing.app.async.mailing.service;

import com.noscompany.mailing.app.async.mailing.properties.Timeout;
import com.noscompany.mailing.app.synchronous.mailing.MailService.MailingResult;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.*;

@AllArgsConstructor
@Log4j2
public class MailingFuture {
    private CompletableFuture<MailingResult> sendingTask;
    private Timeout timeout;

    public MailingResult get() {
        try {
            return sendingTask.get(timeout.getValue(), timeout.getUnit());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("Failed to send email asynchronously, cause: ", e);
            return MailingResult.FAILURE;
        }
    }
}
