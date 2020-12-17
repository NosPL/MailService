package com.noscompany.MailService.app;

import com.noscompany.MailService.app.service.MailService;
import com.noscompany.MailService.app.service.MailDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class MailController {
    private MailService mailSender;

    public ResponseEntity sendMail(@Valid @RequestBody MailDto mailDto) {
        mailSender.sendMail(mailDto);
        return ResponseEntity.accepted().build();
    }


}
