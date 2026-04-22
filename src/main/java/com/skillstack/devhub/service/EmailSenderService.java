package com.skillstack.devhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void sendEmail(String toEmail, String subject, String body){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("devhubfis@gmail.com");
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailSender.send(mailMessage);

        System.out.println("EMAIL ENVIADO CORRECTSMENTE");

    }
}
