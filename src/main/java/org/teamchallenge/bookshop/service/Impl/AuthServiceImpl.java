package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.UserAlreadyExistsException;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.enums.Role;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.AuthService;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setName(registerRequest.getFirstName());
        user.setSurname(registerRequest.getSurname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        Cart cart = cartRepository.save(new Cart());
        user.setCart(cart);
        userRepository.save(user);
        return  AuthenticationResponse.builder()
                .token(JwtService.generateJWT(user.getEmail()))
                .build();
    }

    @Override
    public AuthenticationResponse auth(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        return AuthenticationResponse.builder()
                .token(JwtService.generateJWT(user.getEmail()))
                .build();
    }
}
