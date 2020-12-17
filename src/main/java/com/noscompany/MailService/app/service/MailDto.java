package com.noscompany.MailService.app.service;

import lombok.Value;

import java.util.List;
@Value
public class MailDto {
    private List<String> receivers;
    private String subject;
    private String content;
}
