package com.noscompany.MailService.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
@AllArgsConstructor
public class SendMailService implements MailService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Properties props;

    private void loadCredentials() {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            props = new Properties();
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMail(MailDto mailDto) {

    }

    public void sendMail(String messageString) {

        try {
            MailDto mailDto = objectMapper.readValue(messageString, MailDto.class);

            Auth auth = new Auth();

            // Get the Session object.
            Session session = Session.getDefaultInstance(props, auth);

            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(props.getProperty("from")));

            StringBuilder addresses = new StringBuilder();
            mailDto.getReceivers().forEach(x -> {
                addresses.append(x);
                addresses.append(",");
            });
            addresses.deleteCharAt(addresses.length() - 1);

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses.toString()));

            // Set Subject: header field
            message.setSubject(mailDto.getSubject());

            // Now set the actual message
            message.setText(mailDto.getContent());

            // Send message
            Transport.send(message);

            log.info("Message sent");

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void checkMail() {
        try {
            // Create authentication object
            Auth auth = new Auth();

            // Make a session
            Session session = Session.getDefaultInstance(props, auth);
            Store store = session.getStore("pop3");
            store.connect();

            // Get inbox
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            System.out.println(folder.getMessageCount() + " total messages.");

            // Get array of messages and display them
            Message[] message = folder.getMessages();
            for (int i = 0; i < message.length; i++) {
                System.out.println("---------------------");
                System.out.println("Message # " + (i + 1));
                System.out.println("From: " + message[i].getFrom()[0]);
                System.out.println("Subject: " + message[i].getSubject());
                System.out.println("Message:");
                String content = message[i].getContent().toString();
                System.out.println(content);
            }

            // Close the session
            folder.close(false);
            store.close();
        }
        // This error handling isn't very good
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}