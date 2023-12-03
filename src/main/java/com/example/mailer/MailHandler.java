package com.example.mailer;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MailHandler {

    @Inject
    Mailer mailer;

    public void sendEmail(String to, String subject, String text) {
        mailer.send(Mail.withText(to, subject, text));
    }
}
