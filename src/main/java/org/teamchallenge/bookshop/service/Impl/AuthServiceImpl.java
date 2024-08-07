package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.enums.Role;
import org.teamchallenge.bookshop.exception.*;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.Token;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.repository.TokenRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.AuthService;
import org.teamchallenge.bookshop.service.SendMailService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.teamchallenge.bookshop.constants.ValidationConstants.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtService jwtService;
    private final SendMailService sendMailService;
    private final TokenRepository tokenRepository;


    @Override
    public AuthResponse register(RegisterRequest registerRequest, UUID cartId) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setName(registerRequest.getFirstName());
        user.setSurname(registerRequest.getSurname());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Cart cart = cartId != null
                ? cartRepository.findById(cartId).orElseThrow(NotFoundException::new)
                : createNewCart();

        user.setCart(cart);
        userRepository.save(user);

        sendMailService.sendSuccessRegistrationEmail(registerRequest.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);


        saveRefreshToken(user,refreshToken);
        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Cart createNewCart() {
        Cart cart = new Cart();
        cart.setIsPermanent(true);
        cart.setLastModified(LocalDate.now());
        return cartRepository.save(cart);
    }
@Override
    public AuthResponse login(AuthRequest loginRequest) {
        User user = userRepository.findByEmailOrPhoneNumber(loginRequest.getEmailOrPhone())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPassword();
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user,refreshToken);
    return AuthResponse.builder()
            .token(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    private void saveRefreshToken(User user, String refreshToken) {
        Token token = new Token();
        token.setTokenValue(refreshToken);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        token.setRevoked(false);
        user.addToken(token);
        userRepository.save(user);
    }
@Override
    public void logout(String token) {
        jwtService.revokeToken(token);
    Token tokenEntity = tokenRepository.findByTokenValue(token)
            .orElseThrow(InvalidTokenException::new);
    User user = tokenEntity.getUser();
    user.removeToken(tokenEntity);
    userRepository.save(user);
    }
@Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException(INVALID_RESET_TOKEN);
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(UserNotFoundException::new);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        jwtService.revokeToken(refreshToken);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }


}



