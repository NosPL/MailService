package com.noscompany.mailing.app.retryable.mailing.service;

import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.commons.dto.ReceiverDto;
import com.noscompany.mailing.app.retryable.mailing.entity.Email;
import com.noscompany.mailing.app.retryable.mailing.entity.Receiver;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

class EmailMapper {
    static EmailDto asDto(Email email) {
        Set<Receiver> receivers = email.getReceivers();
        return new EmailDto(
                email.getSender(),
                email.getSubject(),
                email.getContent(),
                asDto(receivers));
    }

    static Set<ReceiverDto> asDto(Set<Receiver> receivers) {
        return receivers
                .stream()
                .map(Receiver::getAddress)
                .map(ReceiverDto::new)
                .collect(toSet());
    }
}
