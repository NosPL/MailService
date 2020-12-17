package com.noscompany.MailService.app.service;

import com.noscompany.MailService.app.repo.AddressRepo;
import com.noscompany.MailService.app.repo.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class SaveNowAndSendLaterMailService implements MailService {
    private final MessageRepo messageRepo;
    private final AddressRepo addressRepo;
    private final MailService mailService;

    @Override
    public void sendMail(MailDto mailDto) { }

    @Scheduled
    public void sendPending() {

    }
}
