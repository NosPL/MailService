package com.noscompany.mailing.app.synchronous.mailing;

import com.noscompany.mailing.app.async.mailing.service.MailServiceCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class SyncMailingConfig {

    private final ApplicationArguments args;
    private Auth auth;
    private Properties properties;

    @Bean
    public MailService mailService() {
        return new SimpleMailingService(messageCreator());
    }

    @Bean
    public MailServiceCreator mailServiceCreator() {
        return () -> new SimpleMailingService(messageCreator());
    }

    @Bean
    public MessageCreator messageCreator() {
        return new MessageCreator(properties(), auth());
    }

    @Bean
    public Properties properties() {
        if (properties != null)
            return properties;
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            this.properties = properties;
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Auth auth() {
        if (auth != null)
            return auth;
        String username = args.getOptionValues("mail.username").stream().findFirst().get();
        String password = args.getOptionValues("mail.password").stream().findFirst().get();
        this.auth = new Auth(username, password);
        return this.auth;
    }
}
