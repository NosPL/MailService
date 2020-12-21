package com.noscompany.mailing.app.synchronous.mailing;

import com.noscompany.mailing.app.commons.dto.EmailDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import java.io.UnsupportedEncodingException;

import static com.noscompany.mailing.app.synchronous.mailing.MailService.MailingResult.FAILURE;
import static com.noscompany.mailing.app.synchronous.mailing.MailService.MailingResult.SUCCESS;

@Log4j2
@AllArgsConstructor
class SimpleMailingService implements MailService {
    private final MessageCreator messageCreator;

    public MailingResult send(EmailDto dto) {
        try {
            log.info("sending email to: " + dto.getReceivers());
            Message msg = messageCreator.createFrom(dto);
            Transport.send(msg);
            log.info("sending finished");
            return SUCCESS;
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.warn("Failed to send message, cause: ", e);
            return FAILURE;
        }
    }
}