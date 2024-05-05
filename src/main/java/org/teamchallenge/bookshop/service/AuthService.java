package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.controller.request.AuthRequest;
import org.teamchallenge.bookshop.controller.request.AuthenticationResponse;
import org.teamchallenge.bookshop.controller.request.RegisterRequest;

public interface AuthService {
    public AuthenticationResponse register (RegisterRequest registerRequest);
    public AuthenticationResponse auth (AuthRequest authRequest);
}
