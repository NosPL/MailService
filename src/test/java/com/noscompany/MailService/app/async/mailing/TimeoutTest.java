package com.noscompany.MailService.app.async.mailing;

import com.noscompany.mailing.app.async.mailing.AsyncMailConfig;
import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.service.MailingFuture;
import com.noscompany.mailing.app.async.mailing.properties.ThreadPoolSize;
import com.noscompany.mailing.app.async.mailing.properties.Timeout;
import fakes.AlwaysFailMailService;
import fakes.SlowMailService;
import io.vavr.control.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.noscompany.mailing.app.synchronous.mailing.MailService.MailingResult.FAILURE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sample.data.SampleData.sampleEmailDto;

public class TimeoutTest {
    private AsyncMailingService asyncMailingService;

    @BeforeEach
    public void init() {
        asyncMailingService = new AsyncMailConfig().asyncMailing(AlwaysFailMailService::new, ThreadPoolSize.of(10));
    }

    @Test
    @DisplayName("Should timeout long running task")
    public void test() {
//        given that mailing takes twice as much as timeout limit
        Timeout timeout = Timeout.of(500, MILLISECONDS);
        asyncMailingService.setTimeout(timeout);
        asyncMailingService.setMailServiceCreator(() -> new SlowMailService(timeout.getValue() * 2, timeout.getUnit()));
//        when mail sending request gets accepted
        Option<MailingFuture> opFuture = asyncMailingService.sendAsync(sampleEmailDto());
        assertTrue(opFuture.isDefined());
//        then sending ends with failure
        assertTrue(opFuture.get().get() == FAILURE);
    }
}
