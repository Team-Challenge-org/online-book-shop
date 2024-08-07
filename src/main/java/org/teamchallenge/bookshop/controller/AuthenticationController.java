package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.OAuth2UserInfo;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthResponse;
import org.teamchallenge.bookshop.model.request.RefreshTokenRequest;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.AuthService;
import org.teamchallenge.bookshop.service.OAuth2Service;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthService authService;
    private final OAuth2Service oAuth2Service;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Id of cart")
            @CookieValue(required = false, name = "cartId") UUID cartId,
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, cartId));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = jwtService.extractTokenFromRequest(request);
        if (token == null) {
            log.warn("No token found in request");
            return ResponseEntity.badRequest().build();
        }

        log.info("Logging out with token: {}", token);
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }

    @PostMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> oauth2AuthenticationSuccess(@RequestBody OAuth2UserInfo oauth2UserInfo) {
        AuthResponse response = oAuth2Service.processOAuth2Authentication(oauth2UserInfo);
        return ResponseEntity.ok(response);
    }
}
