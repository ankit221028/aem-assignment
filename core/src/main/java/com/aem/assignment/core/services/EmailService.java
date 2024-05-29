package com.aem.assignment.core.services;

import org.osgi.service.component.annotations.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * A service for sending emails using JavaMail API.
 */
@Component(service = EmailService.class, immediate = true)
public class EmailService {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_AUTH = "true";
    private static final String SMTP_STARTTLS_ENABLE = "true";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_SSL_PROTOCOLS = "TLSv1.2";
    private static final String USERNAME = "sahankit.0722@gmail.com";
    private static final String PASSWORD = "plzzmttuvltirhli";

    /**
     * Sends an email with the specified parameters.
     *
     * @param to      the recipient email address
     * @param from    the sender email address
     * @param subject the subject of the email
     * @param text    the text content of the email
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendEmail(String to, String from, String subject, String text) {
        boolean flag = false;

        try {
            Session session = createEmailSession();
            Message message = createEmailMessage(session, to, from, subject, text);
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * Creates and configures an email session.
     *
     * @return the configured email session
     */
    private Session createEmailSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST_NAME);
        properties.put("mail.smtp.auth", SMTP_AUTH);
        properties.put("mail.smtp.starttls.enable", SMTP_STARTTLS_ENABLE);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.ssl.protocols", SMTP_SSL_PROTOCOLS);

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    /**
     * Creates an email message with the specified parameters.
     *
     * @param session the email session
     * @param to      the recipient email address
     * @param from    the sender email address
     * @param subject the subject of the email
     * @param text    the text content of the email
     * @return the configured email message
     * @throws MessagingException if an error occurs while creating the email message
     */
    private Message createEmailMessage(Session session, String to, String from, String subject, String text) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
