package com.root.signaturehandler.domain.utils;

import com.root.signaturehandler.domain.interfaces.MailHandlerModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailHandlerAdapter implements MailHandlerModel {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public EmailHandlerAdapter(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendDocumentMailMessage(String emailTo, String documentUrl) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.mailFrom);
        simpleMailMessage.setSubject("New Document attached to you!");
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setText("A new document was uploaded and the uploader selected you as a contact to receive a " +
                "copy on e-mail! \nCheck the document url: " + documentUrl);

        this.javaMailSender.send(simpleMailMessage);
    }

    public void sendNewPasswordMailMessage(String emailTo, String newPassword) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.mailFrom);
        simpleMailMessage.setSubject("New password request!");
        simpleMailMessage.setTo(emailTo);
        simpleMailMessage.setText("A new password was requested to your account, use this new password to sign in: "
                + newPassword + "\nDon't forget to store it on a safe place ;)");

        this.javaMailSender.send(simpleMailMessage);
    }
}
