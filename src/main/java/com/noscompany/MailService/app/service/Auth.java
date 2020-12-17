package com.noscompany.MailService.app.service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Auth extends Authenticator {

    private String username;
    private String password;

    public Auth() {
        super();
    }

    public PasswordAuthentication getPasswordAuthentication() {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            username = prop.getProperty("mail.username");
            password = prop.getProperty("mail.password");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new PasswordAuthentication(username, password);
    }
}