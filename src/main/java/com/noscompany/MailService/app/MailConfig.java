package com.noscompany.MailService.app;

import com.noscompany.MailService.app.repo.AddressRepo;
import com.noscompany.MailService.app.repo.MessageRepo;
import com.noscompany.MailService.app.service.MailService;
import com.noscompany.MailService.app.service.SaveNowAndSendLaterMailService;
import com.noscompany.MailService.app.service.SendMailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Properties;

@Configuration
@EnableJpaRepositories
public class MailConfig {

    public MailService mailService(MessageRepo messageRepo, AddressRepo addressRepo, Properties properties) {
        return new SaveNowAndSendLaterMailService(
                messageRepo,
                addressRepo,
                new SendMailService(properties));
    }
}
