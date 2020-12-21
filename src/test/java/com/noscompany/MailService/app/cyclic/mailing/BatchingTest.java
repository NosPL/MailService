package com.noscompany.MailService.app.cyclic.mailing;

import com.noscompany.mailing.app.async.mailing.AsyncMailConfig;
import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.properties.ThreadPoolSize;
import com.noscompany.mailing.app.retryable.mailing.RetryableMailingConfig;
import com.noscompany.mailing.app.retryable.mailing.entity.EmailRepo;
import com.noscompany.mailing.app.retryable.mailing.entity.InMemoryEmailRepo;
import com.noscompany.mailing.app.retryable.mailing.service.BatchResult;
import com.noscompany.mailing.app.retryable.mailing.service.RetryableMailingService;
import fakes.AlwaysFailMailService;
import fakes.AlwaysSuccessMailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.noscompany.mailing.app.retryable.mailing.properties.BatchSize.of;
import static sample.data.SampleData.sampleEmailDto;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BatchingTest {
    private EmailRepo emailRepo;
    private RetryableMailingService service;
    private AsyncMailingService asyncMailingService;

    @BeforeEach
    public void init() {
        emailRepo = new InMemoryEmailRepo();
        asyncMailingService = new AsyncMailConfig().asyncMailing(AlwaysFailMailService::new, ThreadPoolSize.of(10));
        service = new RetryableMailingConfig().scheduledSending(emailRepo, asyncMailingService);
    }

    @Test
    @DisplayName("should send pending emails in 1 batch")
    public void test4() {
//        given that mailing always succeeds
        service.setMailServiceCreator(AlwaysSuccessMailService::new);
//        and batch size == 5
        int actualBatchSize = service.trySetBatchSize(of(5));
        assertTrue(actualBatchSize == 5);
//        and 4 emails are scheduled for sending
        for (int i = 1; i <= 4; i++)
            service.send(sampleEmailDto());
//        when first batch sending occurs
        BatchResult batchResult = service.sendPendingBatch();
//        then all 4 mails are sent
        assertTrue(batchResult.getSentCount() == 4);
    }

    @Test
    @DisplayName("should send pending emails In 2 batches")
    public void test5() {
//        given that email sending always succeeds
        service.setMailServiceCreator(AlwaysSuccessMailService::new);
//        and batch size == 2
        int actualBatchSize = service.trySetBatchSize(of(2));
        assertTrue(actualBatchSize == 2);
//        and 3 emails are scheduled for sending
        for (int i = 1; i <= 3; i++)
            service.send(sampleEmailDto());
//        when first attempt occurs
        BatchResult batchResult = service.sendPendingBatch();
//        then first 2 emails are sent
        assertTrue(batchResult.getSentCount() == 2);
//        when second attempt occurs
        batchResult = service.sendPendingBatch();
//        then third email is sent
        assertTrue(batchResult.getSentCount() == 1);
    }
}