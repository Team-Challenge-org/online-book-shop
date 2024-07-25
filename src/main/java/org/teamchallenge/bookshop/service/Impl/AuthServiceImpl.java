package org.teamchallenge.bookshop.service.Impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.enums.Role;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.exception.UserAlreadyExistsException;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.Token;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.model.request.RegisterRequest;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.repository.TokenRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.AuthService;
import org.teamchallenge.bookshop.service.OAuth2Service;
import org.teamchallenge.bookshop.service.SendMailService;

import java.time.LocalDate;
import java.util.UUID;

import static org.teamchallenge.bookshop.constants.ValidationConstants.LOGOUT_FAILED;
import static org.teamchallenge.bookshop.constants.ValidationConstants.LOGOUT_URL_NULL;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CartRepository cartRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final SendMailService sendMailService;
    private final OAuth2Service oAuth2Service;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest, UUID cartId) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setName(registerRequest.getFirstName());
        user.setSurname(registerRequest.getSurname());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
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
        sendMailService.sendSuccessRegistrationEmail(registerRequest.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtService.generateJWT(user))
                .build();
    }

    @Override
    public AuthenticationResponse auth(AuthRequest authRequest) {
        User user = userRepository.findByEmailOrPhoneNumber(authRequest.getEmailOrPhone())
                .orElseThrow(UserNotFoundException::new);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmailOrPhone(),
                        authRequest.getPassword()
                )
        );

        return AuthenticationResponse.builder()
                .token(jwtService.generateJWT(user))
                .build();
    }

    @Override
    public String logout(HttpServletRequest request, String authType) {
        String AuthTypeIgnoreCase = authType.toLowerCase();

        return switch (AuthTypeIgnoreCase) {
            case "jwt" -> {
                logoutJwt(request);
                yield null;
            }
            case "oauth2" -> logoutOAuth2(request);
            default -> throw new IllegalArgumentException("Unsupported authType: " + authType);
        };
    }


    private void logoutJwt(HttpServletRequest request) {
        String jwt = jwtService.extractTokenFromRequest(request);
        Token token = jwtService.blacklistToken(jwt);
        tokenRepository.save(token);
    }

    private String logoutOAuth2(HttpServletRequest request) {
        String provider = request.getParameter("provider");
        if (provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException(LOGOUT_FAILED);
        }
        String logoutUrl = oAuth2Service.getLogoutUrl(provider);
        if (logoutUrl == null || logoutUrl.isEmpty()) {
            throw new IllegalArgumentException(LOGOUT_URL_NULL + provider);
        }
        return logoutUrl;
    }
}



