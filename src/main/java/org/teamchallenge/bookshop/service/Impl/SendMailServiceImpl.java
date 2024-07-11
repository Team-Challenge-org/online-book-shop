package org.teamchallenge.bookshop.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.service.SendMailService;

import java.util.regex.Pattern;

import static org.teamchallenge.bookshop.constants.ValidationConstants.EMAIL_REGEXP;
import static org.teamchallenge.bookshop.constants.ValidationConstants.INVALID_EMAIL_ADDRESS;

@Service
public class SendMailServiceImpl implements SendMailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;


    private static final Pattern EMAIL_REGEX_PATTERN =
            Pattern.compile(EMAIL_REGEXP, Pattern.CASE_INSENSITIVE);

    @Autowired
    public SendMailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String send(String mail) {
        if (!isValidEmail(mail)) {
            return INVALID_EMAIL_ADDRESS;
        }

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Welcome from Team Challenge Library");
        simpleMailMessage.setText("Hello from Team Challenge Library, Happy to see you on our website!");
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setFrom(mailAddress);
        javaMailSender.send(simpleMailMessage);
        return "Mail sent successfully";
    }
    private boolean isValidEmail(String email) {
        return email != null && EMAIL_REGEX_PATTERN.matcher(email).matches();
    }
}