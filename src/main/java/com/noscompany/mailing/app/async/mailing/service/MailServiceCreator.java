package com.noscompany.mailing.app.async.mailing.service;

import com.noscompany.mailing.app.synchronous.mailing.MailService;

/**
 * AsyncMailingService must be able to obtain new instances of MailService for each thread
 */
@FunctionalInterface
public interface MailServiceCreator {
    MailService getNewInstance();
}
