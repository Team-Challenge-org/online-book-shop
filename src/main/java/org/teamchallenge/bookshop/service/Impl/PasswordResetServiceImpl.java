package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamchallenge.bookshop.enums.TokenValidationResult;
import org.teamchallenge.bookshop.exception.InvalidTokenException;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.PasswordResetToken;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.PasswordTokenRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.service.PasswordResetService;
import org.teamchallenge.bookshop.service.SendMailService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final SendMailService sendMailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void initiatePasswordReset(String userEmail) {
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(userEmail, token);
        String resetUrl = "https://online-book-shop-client.onrender.com/api/v1/user/reset_password?token=" + token;
        sendMailService.sendResetTokenEmail(resetUrl, token, userEmail);
    }


    public void createPasswordResetTokenForUser(String userEmail, String token) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        PasswordResetToken existingToken = passwordTokenRepository.findByUser(user);
        if (existingToken != null) {
            updateToken(existingToken, token);
        } else {
            PasswordResetToken myToken = new PasswordResetToken();
            myToken.setToken(token);
            myToken.setUser(user);
            myToken.setExpiryDate(calculateExpiryDate());
            passwordTokenRepository.save(myToken);
        }
    }
    @Override
    public void saveNewPassword(String token, String newPassword) {
        String validationResult = validatePasswordResetToken(token);
        if (!TokenValidationResult.VALID.name().equals(validationResult)) {
            throw new InvalidTokenException();
        }
        User user = getUserByPasswordResetToken(token)
                .orElseThrow(UserNotFoundException::new);

        changeUserPassword(user, newPassword);
        passwordTokenRepository.deleteByToken(token);
    }

    public String validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passTokenOpt = passwordTokenRepository.findByToken(token);
        if (passTokenOpt.isEmpty()) {
            return TokenValidationResult.INVALID.name();
        }
        PasswordResetToken passToken = passTokenOpt.get();
        return isTokenExpired(passToken) ? TokenValidationResult.EXPIRED.name() : TokenValidationResult.VALID.name();
    }

    public Optional<User> getUserByPasswordResetToken(String token) {
        return passwordTokenRepository.findByToken(token)
                .filter(passToken -> !isTokenExpired(passToken))
                .map(PasswordResetToken::getUser);
    }

    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return new Date().after(passToken.getExpiryDate());
    }

    private void updateToken(PasswordResetToken existingToken, String token) {
        existingToken.setToken(token);
        existingToken.setExpiryDate(calculateExpiryDate());
        passwordTokenRepository.save(existingToken);
    }

    private Date calculateExpiryDate() {
        return new Date(System.currentTimeMillis() + PasswordResetToken.EXPIRATION_TIME_IN_MILLIS);
    }
}