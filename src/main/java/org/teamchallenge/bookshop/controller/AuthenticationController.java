package org.teamchallenge.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponse> auth (@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.auth(request));
    }

}
