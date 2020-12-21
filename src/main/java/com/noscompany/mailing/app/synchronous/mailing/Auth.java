package com.noscompany.mailing.app.synchronous.mailing;

import lombok.AllArgsConstructor;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@AllArgsConstructor
class Auth extends Authenticator {
    private String username;
    private String password;

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}