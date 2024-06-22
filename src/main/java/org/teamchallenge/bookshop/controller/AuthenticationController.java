package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
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
@CrossOrigin(maxAge = 3600, origins = "*")
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

}
