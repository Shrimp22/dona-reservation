package com.example.resources;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/mail")                                                          
public class MailResource {

    @Inject Mailer mailer;       

    @GET
    @Blocking
    public void sendEmail(String to, String subject, String text) {
        mailer.send(Mail.withText(to, subject, text));
    }

}