package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.AuthService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
public class AuthenticationController {
    private final AuthService authService;
    private final JwtService jwtService;
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

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletRequest request) {
        return ResponseEntity.ok(authService.refresh(request,refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(description = "Get expired JWT token")
    @PostMapping("expiredAccess")
    public ResponseEntity<String> expiredAccessToken(@RequestBody AuthRequest authRequest) {
        return jwtService.generateExpiredAccessToken(authRequest);
    }

    @Operation(description = "Get expired JWT token")
    @PostMapping("expiredRefresh")
    public ResponseEntity<String> expired() {
        return jwtService.generateExpiredRefreshToken();
    }
}
