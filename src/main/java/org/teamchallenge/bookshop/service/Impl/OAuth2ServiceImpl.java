package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.Oauth2.UserCreationService;
import org.teamchallenge.bookshop.dto.OAuth2UserInfo;
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
    private final UserCreationService userCreationService;

    @Override
    public AuthenticationResponse processOAuth2Authentication(OAuth2UserInfo oauth2UserInfo) {
        User user = userRepository.findByEmailAndProvider(oauth2UserInfo.getEmail(), oauth2UserInfo.getProvider())
                .orElseGet(() -> userRepository.findByProviderId(oauth2UserInfo.getProviderId())
                        .orElseGet(() -> userCreationService.createNewUser(oauth2UserInfo)));

        user = updateUserInfo(user, oauth2UserInfo);

        return AuthenticationResponse.builder()
                .token(jwtService.generateJWT(user))
                .build();
    }

    private User updateUserInfo(User existingUser, OAuth2UserInfo oauth2UserInfo) {
        existingUser.setName(oauth2UserInfo.getName());
        existingUser.setProviderId(oauth2UserInfo.getProviderId());
        return userRepository.save(existingUser);
    }
}
