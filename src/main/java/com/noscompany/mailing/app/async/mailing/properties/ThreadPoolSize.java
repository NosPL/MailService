package com.noscompany.mailing.app.async.mailing.properties;

import lombok.Value;

@Value(staticConstructor = "of")
public class ThreadPoolSize {
    int value;
}
