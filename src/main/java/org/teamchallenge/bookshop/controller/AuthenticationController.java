package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
public class AuthenticationController {
    private final AuthService authService;

    @Operation(description = "Register our user")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(description = "Login our user")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> auth (@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.auth(request));
    }

}
