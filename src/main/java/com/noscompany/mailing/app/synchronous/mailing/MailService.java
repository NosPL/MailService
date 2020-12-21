package com.noscompany.mailing.app.synchronous.mailing;

import com.noscompany.mailing.app.commons.dto.EmailDto;

@FunctionalInterface
public interface MailService {

    enum MailingResult { SUCCESS, FAILURE }

    MailingResult send(EmailDto emailDto);
}
