package org.teamchallenge.bookshop.service;

public interface SendMailService {
    String send(String mail);

     void sendResetTokenEmail(String resetUrl, String token, String userEmail);
}