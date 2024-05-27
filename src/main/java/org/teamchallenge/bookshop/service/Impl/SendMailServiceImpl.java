package org.teamchallenge.bookshop.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.service.SendMailService;

@Service
public class SendMailServiceImpl implements SendMailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;

    @Autowired
    public SendMailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public String send(String mail) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Welcome from Team Challenge Library");
        simpleMailMessage.setText("Hello from Team Challenge Library, Happy to see you on our website!");
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setFrom(mailAddress);
        javaMailSender.send(simpleMailMessage);
        return "Mail sent successfully";
    }
}