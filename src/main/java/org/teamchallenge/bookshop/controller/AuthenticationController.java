package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.service.AuthService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
            @Parameter(description = "Id of cart")
            @CookieValue(required = false, name = "cartId") UUID cartId,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, cartId));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> auth (@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.auth(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
