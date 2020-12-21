package com.noscompany.mailing.app.retryable.mailing.web;

import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.properties.Timeout;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/queued")
@AllArgsConstructor
public class RetryableMailingController {
    private AsyncMailingService asyncMailingService;

    @GetMapping("/timeout")
    ResponseEntity<Timeout> getTimeout() {
        Timeout timeout = asyncMailingService.getTimeout();
        return ResponseEntity.ok(timeout);
    }

    @PostMapping("/timeout")
    ResponseEntity<Timeout> setTimeout(@RequestParam("value") int value, @RequestParam("unit") String unit) {
        TimeUnit timeUnit = TimeUnit.valueOf(unit.toUpperCase());
        Timeout timeout = Timeout.of(value, timeUnit);
        asyncMailingService.setTimeout(timeout);
        return ResponseEntity.ok(timeout);
    }

    @PostMapping("/timeout2")
    ResponseEntity<Timeout> setTimeout(@RequestParam("value") int value, @RequestParam("unit") TimeUnit timeUnit) {
        Timeout timeout = Timeout.of(value, timeUnit);
        asyncMailingService.setTimeout(timeout);
        return ResponseEntity.ok(timeout);
    }
}
