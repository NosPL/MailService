package com.noscompany.mailing.app.retryable.mailing.properties;

import lombok.NonNull;

public class BatchSize {
    @NonNull
    private final int value;

    private BatchSize(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static BatchSize of(int value) {
        if (value < 1) throw new RuntimeException("Minimal number of mailing batch size must be at least 1");
        return new BatchSize(value);
    }
}
