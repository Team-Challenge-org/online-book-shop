package org.teamchallenge.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.dto.OAuth2UserInfo;
import org.teamchallenge.bookshop.enums.Role;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.OAuth2Service;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse processOAuth2Authentication(OAuth2UserInfo oauth2UserInfo) {
        User user = userRepository.findByEmail(oauth2UserInfo.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setName(oauth2UserInfo.getName());
                    newUser.setEmail(oauth2UserInfo.getEmail());
                    newUser.setProvider(oauth2UserInfo.getProvider());
                    newUser.setProviderId(oauth2UserInfo.getProviderId());
                    newUser.setRole(Role.USER);
                    return userRepository.save(newUser);
                });

        return AuthenticationResponse.builder()
                .token(jwtService.generateJWT(user))
                .build();
    }
}
