package com.noscompany.mailing.app.async.mailing.web;

import com.noscompany.mailing.app.async.mailing.service.AsyncMailingService;
import com.noscompany.mailing.app.async.mailing.service.MailingFuture;
import com.noscompany.mailing.app.async.mailing.properties.Timeout;
import com.noscompany.mailing.app.commons.dto.EmailDto;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@RestController
@RequestMapping("/async")
@AllArgsConstructor
public class AsyncMailingController {
    private AsyncMailingService asyncMailingService;

    @PostMapping
    ResponseEntity send(@Valid @RequestBody EmailDto dto) {
        Option<MailingFuture> opFuture = asyncMailingService.sendAsync(dto);
        if (opFuture.isEmpty())
            return new ResponseEntity(TOO_MANY_REQUESTS);
        else
            return new ResponseEntity(ACCEPTED);
    }

    @GetMapping("/timeout")
    ResponseEntity<Timeout> getTimeout() {
        Timeout timeout = asyncMailingService.getTimeout();
        return ResponseEntity.ok(timeout);
    }

    @PostMapping("/timeout")
    ResponseEntity<Timeout> setTimeout(@RequestParam("value") int value, @RequestParam("unit") String unit) {
        return Timeout.of(value, unit)
                .peek(asyncMailingService::setTimeout)
                .map(ResponseEntity::ok)
                .getOrElse(ResponseEntity.badRequest().build());
    }
}