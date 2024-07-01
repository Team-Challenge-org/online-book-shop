package org.teamchallenge.bookshop.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.enums.Role;
import org.teamchallenge.bookshop.enums.TokenStatus;
import org.teamchallenge.bookshop.exception.*;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.AuthService;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final JwtService jwtService;

    @Autowired
    EntityManager entityManager;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest, UUID cartId) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setName(registerRequest.getFirstName());
        user.setSurname(registerRequest.getSurname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        Cart cart;
        if (cartId != null) {
            cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
            user.setCart(cart);
        } else {
            cart = new Cart();
            cart.setIsPermanent(true);
            cart.setLastModified(LocalDate.now());
            cartRepository.save(cart);
            user.setCart(cart);
        }
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken())
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
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken())
                .build();
    }

    @Override
    public AuthenticationResponse refresh(HttpServletRequest request, String refreshToken) {
        String accessToken = jwtService.extractTokenFromRequest(request);
        if (jwtService.checkToken(refreshToken) == TokenStatus.VALID && jwtService.checkToken(accessToken) == TokenStatus.EXPIRED) {
            User user = userRepository
                    .findByEmail(jwtService.extractUsername(accessToken))
                    .orElseThrow(UserNotFoundException::new);
            accessToken = jwtService.generateAccessToken(user);
        } else if (jwtService.checkToken(refreshToken) == TokenStatus.BLACKLISTED) {
            throw new TokenBlacklistedException("Seems like refresh token is blacklisted");
        } else if (jwtService.checkToken(refreshToken) == TokenStatus.EXPIRED) {
            throw new TokenExpiredException("Refresh token is expired. Log in again");
        }
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        String jwt = jwtService.extractTokenFromRequest(request);
        jwtService.blacklistToken(jwt);
    }
}
