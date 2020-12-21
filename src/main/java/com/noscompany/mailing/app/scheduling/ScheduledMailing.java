package com.noscompany.mailing.app.scheduling;

import com.noscompany.mailing.app.retryable.mailing.service.RetryableMailingService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class ScheduledMailing {
    private RetryableMailingService retryableMailingService;

    @Scheduled(fixedDelay = 1000)
    public void retryableMailing() {
        retryableMailingService.sendPendingBatch();
    }
}
