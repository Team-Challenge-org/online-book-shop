package org.teamchallenge.bookshop.service.Impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.service.SendMailService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static org.teamchallenge.bookshop.constants.ValidationConstants.EMAIL_REGEXP;
import static org.teamchallenge.bookshop.constants.ValidationConstants.INVALID_EMAIL_ADDRESS;

@Service
@AllArgsConstructor
public class SendMailServiceImpl implements SendMailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailAddress;


    private static final Pattern EMAIL_REGEX_PATTERN =
            Pattern.compile(EMAIL_REGEXP, Pattern.CASE_INSENSITIVE);

    @Autowired
    public SendMailServiceImpl(JavaMailSender javaMailSender, UserRepository userRepository) {
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

    public void sendResetTokenEmail(String resetUrl, String token, String userEmail) {
        try {
            String htmlContent = loadEmailTemplate();
            htmlContent = htmlContent.replace("{{token}}", token)
                    .replace("{{reset_link}}", resetUrl);

            sendHtmlEmail(userEmail, htmlContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }

    @Override
    public String sendSuccessRegistrationEmail(String mail) {
        if (!isValidEmail(mail)) {
            return INVALID_EMAIL_ADDRESS;
        }

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Welcome to Team Challenge");
        simpleMailMessage.setText("Hello from Team Challenge, You successfully registered!");
        simpleMailMessage.setTo(mail);
        simpleMailMessage.setFrom(mailAddress);
        javaMailSender.send(simpleMailMessage);
        return "Mail sent successfully";
    }

    private String loadEmailTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("TCL.html");
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    private void sendHtmlEmail(String to, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mailAddress);
            helper.setTo(to);
            helper.setSubject("Скидання пароля");
            helper.setText(htmlContent, true);
            ClassPathResource logoImage = new ClassPathResource("logo TCL.png");
            helper.addInline("logoImage", logoImage);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}