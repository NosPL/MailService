package com.noscompany.mailing.app.synchronous.mailing;

import com.noscompany.mailing.app.commons.dto.EmailDto;
import com.noscompany.mailing.app.commons.dto.ReceiverDto;
import io.vavr.API;
import lombok.AllArgsConstructor;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.function.Function;

@AllArgsConstructor
class MessageCreator {
    Properties props;
    Auth auth;

    Message createFrom(EmailDto emailDto) throws MessagingException, UnsupportedEncodingException, AddressException {
        Session session = Session.getDefaultInstance(props, auth);
        Message message = new MimeMessage(session);
        message.setFrom(senderAddress(emailDto));
        message.setRecipients(Message.RecipientType.TO, recipientsAddresses(emailDto));
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getContent());
        return message;
    }

    private InternetAddress senderAddress(EmailDto emailDto) throws UnsupportedEncodingException {
        String senderName = emailDto.getSenderName();
        String address = (String) props.get("mail.username");
        return new InternetAddress(address, senderName);
    }

    private InternetAddress[] recipientsAddresses(EmailDto dto) throws AddressException {
        Function<String, InternetAddress> toInternetAddress = API.unchecked((String s) -> new InternetAddress(s));
        return dto
                .getReceivers().stream()
                .map(ReceiverDto::getAddress)
                .map(toInternetAddress)
                .toArray(InternetAddress[]::new);
    }
}