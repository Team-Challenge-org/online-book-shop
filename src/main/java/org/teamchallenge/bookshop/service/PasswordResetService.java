package org.teamchallenge.bookshop.service;

import org.springframework.transaction.annotation.Transactional;

public interface PasswordResetService {
    @Transactional
    void initiatePasswordReset(String userEmail);
    @Transactional
    void saveNewPassword(String token, String newPassword);
}
