package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.Oauth2.UserCreationService;
import org.teamchallenge.bookshop.dto.OAuth2UserInfo;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.AuthResponse;
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
    public AuthResponse processOAuth2Authentication(OAuth2UserInfo oauth2UserInfo) {
        User user = userRepository.findByEmail(oauth2UserInfo.getEmail())
                .map(existingUser -> updateExistingUser(existingUser, oauth2UserInfo))
                .orElseGet(() -> userCreationService.createNewUser(oauth2UserInfo));


        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oauth2UserInfo) {
        existingUser.setName(oauth2UserInfo.getName());
        existingUser.setSurname(oauth2UserInfo.getSurname());
        existingUser.setProvider(oauth2UserInfo.getProvider());
        existingUser.setProviderId(oauth2UserInfo.getProviderId());
        return userRepository.save(existingUser);
    }

}
