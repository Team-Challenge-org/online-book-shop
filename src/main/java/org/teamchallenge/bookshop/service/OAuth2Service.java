package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.OAuth2UserInfo;
import org.teamchallenge.bookshop.model.request.AuthResponse;
public interface OAuth2Service {
    AuthResponse processOAuth2Authentication(OAuth2UserInfo oauth2UserInfo);
}
