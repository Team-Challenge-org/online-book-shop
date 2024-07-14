package org.teamchallenge.bookshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.PasswordResetRequestDto;
import org.teamchallenge.bookshop.enums.TokenValidationResult;
import org.teamchallenge.bookshop.exception.ErrorResponseException;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.PasswordTokenRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.SendMailService;
import org.teamchallenge.bookshop.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.teamchallenge.bookshop.constants.ValidationConstants.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class UserController {
    private final UserService userService;
    private final SendMailService sendMailService;
    private final JwtService jwtService;
    private final PasswordTokenRepository passwordTokenRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/favourites/add")
    public ResponseEntity<Void> addBookToFavourites(@RequestParam Long id) {
        userService.addBookToFavourites(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/favourites/delete")
    public ResponseEntity<Void> deleteBookFromFavourites(@RequestParam Long id) {
        userService.deleteBookFromFavourites(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/favourites")
    public ResponseEntity<List<BookDto>> getUserFavourites() {
        return ResponseEntity.ok(userService.getFavouriteBooks());
    }
    @Transactional
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(HttpServletRequest request, @RequestParam String userEmail) {
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(userEmail, token);
        String resetUrl = constructResetUrl(request, token);
        sendMailService.sendResetTokenEmail(resetUrl, userEmail);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    private String constructResetUrl(HttpServletRequest request, String token) {
        String appUrl = getAppUrl(request);
        return appUrl + "/api/v1/user/resetPassword?token=" + token;
    }
    private String getAppUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    @GetMapping("/resetPassword")
    public ResponseEntity<String> showResetPasswordForm(@RequestParam String token) {
        String validationResult = jwtService.validatePasswordResetToken(token);
        if (!TokenValidationResult.VALID.name().equals(validationResult)) {
            throw new ErrorResponseException(INVALID_RESET_TOKEN + validationResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(PASSWORD_RESTED);
    }
        @Transactional
    @PostMapping("/savePassword")
    public ResponseEntity<String> savePassword(@Valid @RequestBody PasswordResetRequestDto requestDto) {
        String token = requestDto.getToken();
        String newPassword = requestDto.getNewPassword();
        User user = userService.getUserByPasswordResetToken(token)
                .orElseThrow(() -> new ErrorResponseException(USER_NOT_FOUND + " for token: " + token, HttpStatus.BAD_REQUEST));

        userService.changeUserPassword(user, newPassword);
        passwordTokenRepository.deleteByToken(token);

        return ResponseEntity.ok(PASSWORD_SAVED);
    }

}