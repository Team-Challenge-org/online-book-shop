package org.teamchallenge.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.service.PasswordResetService;
import org.teamchallenge.bookshop.service.UserService;

import java.util.List;

import static org.teamchallenge.bookshop.constants.ValidationConstants.PASSWORD_SAVED;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordResetService passwordResetService;

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

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword( @RequestParam String userEmail) {
        passwordResetService.initiatePasswordReset(userEmail);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping("/savePassword")
    public ResponseEntity<String> savePassword(@RequestParam String token, @RequestParam String newPassword) {
        passwordResetService.saveNewPassword(token, newPassword);
        return ResponseEntity.ok(PASSWORD_SAVED);
    }

}