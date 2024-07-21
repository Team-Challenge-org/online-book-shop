package org.teamchallenge.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;

import java.util.UUID;

public interface AuthService {
     AuthenticationResponse register (RegisterRequest registerRequest, UUID cartID);
     AuthenticationResponse auth (AuthRequest authRequest);
     void logout(HttpServletRequest request);
}
