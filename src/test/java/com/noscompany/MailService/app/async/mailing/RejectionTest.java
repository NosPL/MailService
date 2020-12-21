package com.noscompany.MailService.app.async.mailing;

import com.noscompany.mailing.app.async.mailing.AsyncMailConfig;
import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.properties.ThreadPoolSize;
import fakes.AlwaysFailMailService;
import fakes.SlowMailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.noscompany.mailing.app.async.mailing.properties.Timeout.of;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sample.data.SampleData.sampleEmailDto;

public class RejectionTest {
    private AsyncMailingService asyncMailingService;

    @BeforeEach
    public void init() {
        asyncMailingService = new AsyncMailConfig().asyncMailing(AlwaysFailMailService::new, ThreadPoolSize.of(10));
    }

    @Test
    @DisplayName("should accept requests under the limit if empty")
    public void test1() {
//        given that service is empty
        assertTrue(asyncMailingService.isEmpty());
//        and email sending is veeery slow
        asyncMailingService.setMailServiceCreator(() -> new SlowMailService(100, DAYS));
//        and timeout is veeeery long
        asyncMailingService.setTimeout(of(100, DAYS));
//        when max thread count == x
        int x = asyncMailingService.threadPoolMaxSize();
//        then x concurrent mailing task should be accepted
        for (int i = 1; i <= x; i++) {
            assertTrue(asyncMailingService.sendAsync(sampleEmailDto()).isDefined());
        }
    }

    @Test
    @DisplayName("Should reject mailing requests when full")
    public void test2() {
//        when service is filled to the max with requests
        test1();
//        then service should be full
        assertTrue(asyncMailingService.isFull());
//        and every next request should be rejected
        assertTrue(asyncMailingService.sendAsync(sampleEmailDto()).isEmpty());
    }
}
