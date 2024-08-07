package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;

import java.util.UUID;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest, UUID cartId);
    AuthResponse login(AuthRequest loginRequest);
    AuthResponse refreshToken(String refreshToken);
    void logout(String token);
}
