package com.noscompany.mailing.app.async.mailing.properties;

import io.vavr.control.Option;
import lombok.*;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Timeout {
    private int value;
    private TimeUnit unit;

    public int getValue() {
        return this.value;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public Timeout multiplyBy(int multiplier) {
        return new Timeout(value * multiplier, unit);
    }

    public static Timeout of(int value, TimeUnit unit) {
        return new Timeout(value, unit);
    }

    public static Option<Timeout> of(int value, String timeUnitStr) {
        if (value < 0)
            return Option.none();
        try {
            TimeUnit timeUnit = TimeUnit.valueOf(timeUnitStr.toUpperCase());
            Timeout timeout = new Timeout(value, timeUnit);
            return Option.of(timeout);
        } catch (IllegalArgumentException e) {
            return Option.none();
        }
    }
}
