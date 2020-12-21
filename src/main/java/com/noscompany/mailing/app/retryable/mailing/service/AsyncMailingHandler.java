package com.noscompany.mailing.app.retryable.mailing.service;

import com.noscompany.mailing.app.async.mailing.service.MailingFuture;
import com.noscompany.mailing.app.retryable.mailing.entity.Email;
import com.noscompany.mailing.app.retryable.mailing.properties.AttemptLimit;
import com.noscompany.mailing.app.synchronous.mailing.MailService.MailingResult;
import lombok.AllArgsConstructor;

/**
 * It changes state of Email entity based on asynchronous sending result
 */
@AllArgsConstructor
class AsyncMailingHandler {
    private Email email;
    private MailingFuture opFuture;
    private AttemptLimit attemptLimit;

    /**
     * Modifies the state of the Email entity depending on the sending result
     *
     * @return Email entity
     */
    Email waitForFinish() {
        MailingResult result = opFuture.get();
        if (result == MailingResult.SUCCESS)
            email.markAsSent();
        else
            email.failedToSend(attemptLimit);
        return email;
    }
}