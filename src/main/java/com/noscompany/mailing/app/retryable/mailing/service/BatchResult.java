package com.noscompany.mailing.app.retryable.mailing.service;

import com.noscompany.mailing.app.retryable.mailing.entity.Email;
import lombok.Value;

import java.util.Collection;

@Value
public class BatchResult {
    int size;
    int sentCount;

    public static BatchResult from(Collection<Email> emails) {
        int size = emails.size();
        int sentCount = (int) emails.stream().filter(Email::isSent).count();
        return new BatchResult(size, sentCount);
    }
}