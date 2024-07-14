package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.service.SendMailService;

import java.util.regex.Pattern;

import static org.teamchallenge.bookshop.constants.ValidationConstants.EMAIL_REGEXP;
import static org.teamchallenge.bookshop.constants.ValidationConstants.INVALID_EMAIL_ADDRESS;

@Service
@AllArgsConstructor
public class SendMailServiceImpl implements SendMailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String mailAddress;


    private static final Pattern EMAIL_REGEX_PATTERN =
            Pattern.compile(EMAIL_REGEXP, Pattern.CASE_INSENSITIVE);

    @Autowired
    public SendMailServiceImpl(JavaMailSender javaMailSender, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
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

    public void sendResetTokenEmail(String resetUrl, String userEmail) {

        String emailContent = "Please click the link below to reset your password:\n" + resetUrl;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject("Reset your password");
        email.setText(emailContent);
        email.setFrom(mailAddress);
        sendEmail(email);
    }

    private void sendEmail(SimpleMailMessage email) {
        try {
            javaMailSender.send(email);
        } catch (MailException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}