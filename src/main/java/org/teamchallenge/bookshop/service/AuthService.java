package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;

public interface AuthService {
     AuthenticationResponse register (RegisterRequest registerRequest);
     AuthenticationResponse auth (AuthRequest authRequest);
}
