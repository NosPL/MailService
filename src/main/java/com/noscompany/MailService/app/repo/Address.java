package com.noscompany.MailService.app.repo;

import com.noscompany.MailService.app.commons.BaseEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;

@Entity
@Getter
public class Address extends BaseEntity {
    @Email
    private String address;

    @ManyToOne
    private MessageEntity mail;
}
