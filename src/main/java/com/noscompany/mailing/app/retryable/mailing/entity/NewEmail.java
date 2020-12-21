package com.noscompany.mailing.app.retryable.mailing.entity;

import com.noscompany.mailing.app.commons.dto.ReceiverDto;
import com.noscompany.mailing.app.commons.dto.EmailDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class NewEmail {
    public static Email createFrom(EmailDto emailDto, LocalDateTime createdAt) {
        Set<Receiver> receivers = emailDto
                .getReceivers()
                .stream()
                .map(ReceiverDto::getAddress)
                .map(Receiver::new)
                .collect(Collectors.toSet());
        return new Email(emailDto.getSenderName(), emailDto.getSubject(), emailDto.getContent(), createdAt, receivers);
    }
}