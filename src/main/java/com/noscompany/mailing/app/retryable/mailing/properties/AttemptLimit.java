package com.noscompany.mailing.app.retryable.mailing.properties;

import lombok.NonNull;


public class AttemptLimit {
    @NonNull
    private final int value;

    private AttemptLimit(@NonNull int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static AttemptLimit of(int attemptLimit) {
        if (attemptLimit < 1) throw new RuntimeException("Minimal value of mailing attempt limit must be at least 1");
        return new AttemptLimit(attemptLimit);
    }
}
