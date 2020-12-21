package com.noscompany.MailService.app.cyclic.mailing;

import com.noscompany.mailing.app.async.mailing.AsyncMailConfig;
import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.properties.ThreadPoolSize;
import com.noscompany.mailing.app.retryable.mailing.RetryableMailingConfig;
import com.noscompany.mailing.app.retryable.mailing.entity.Email;
import com.noscompany.mailing.app.retryable.mailing.entity.EmailRepo;
import com.noscompany.mailing.app.retryable.mailing.entity.InMemoryEmailRepo;
import com.noscompany.mailing.app.retryable.mailing.service.RetryableMailingService;
import fakes.AlwaysFailMailService;
import fakes.AlwaysSuccessMailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.noscompany.mailing.app.retryable.mailing.properties.AttemptLimit.of;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sample.data.SampleData.sampleEmailDto;

public class AttemptLimitTest {
    private EmailRepo emailRepo;
    private RetryableMailingService retryableMailingService;
    private AsyncMailingService asyncMailingService;

    @BeforeEach
    public void init() {
        emailRepo = new InMemoryEmailRepo();
        asyncMailingService = new AsyncMailConfig().asyncMailing(AlwaysFailMailService::new, ThreadPoolSize.of(20));
        retryableMailingService = new RetryableMailingConfig().scheduledSending(emailRepo, asyncMailingService);
    }

    @Test
    @DisplayName("should successfully send an email on first attempt")
    void test1() {
//        given that mailing always succeeds
        retryableMailingService.setMailServiceCreator(AlwaysSuccessMailService::new);
//        and 1 email is scheduled for sending
        retryableMailingService.send(sampleEmailDto());
//        when there is 1 batch sending attempt
        retryableMailingService.sendPendingBatch();
//        then
        Email mail = OneAndOnlyEmailInDb();
        assertTrue(mail.isSent());
        assertTrue(mail.getSendingAttempts() == 1);
    }

    @Test
    @DisplayName("should schedule email for resending if failed on first attempt but attempt limit == 2")
    void shouldScheduleForLastResendingAttempt() {
//        given that mailing always fails
        retryableMailingService.setMailServiceCreator(AlwaysFailMailService::new);
//        and mailing attempt limit == 2
        retryableMailingService.setAttemptLimit(of(2));
//        and email was scheduled for sending
        retryableMailingService.send(sampleEmailDto());
//        when first mailing attempt occurs
        retryableMailingService.sendPendingBatch();
//        then
        Email email = OneAndOnlyEmailInDb();
        assertTrue(email.isScheduledForResending());
        assertTrue(email.getSendingAttempts() == 1);
    }

    @Test
    @DisplayName("should send an email on second attempt if failed on first attempt but did not reach attempt limit")
    void test3() {
//        given that email was scheduled for resending
        shouldScheduleForLastResendingAttempt();
//        when successful attempt occurs
        retryableMailingService.setMailServiceCreator(AlwaysSuccessMailService::new);
        retryableMailingService.sendPendingBatch();
//        then
        Email email = OneAndOnlyEmailInDb();
        assertTrue(email.isSent());
        assertTrue(email.getSendingAttempts() == 2);
    }

    @Test
    @DisplayName("should discard an email after reaching attempt limit")
    void test4() {
//        given that mailing always fails
        retryableMailingService.setMailServiceCreator(AlwaysFailMailService::new);
//        and email was scheduled for 1 last resending attempt
        shouldScheduleForLastResendingAttempt();
//        when another mailing attempt occurs
        retryableMailingService.sendPendingBatch();
//        then
        Email email = OneAndOnlyEmailInDb();
        assertTrue(email.isDiscarded());
        assertTrue(email.getSendingAttempts() == 2);
    }

    private Email OneAndOnlyEmailInDb() {
        Set<Email> mails = emailRepo.findAllEager();
        assertTrue(mails.size() == 1);
        return mails.stream().findFirst().get();
    }
}