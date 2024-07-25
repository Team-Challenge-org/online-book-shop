package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.OAuth2UserInfo;
import org.teamchallenge.bookshop.model.request.AuthenticationResponse;
public interface OAuth2Service {
    AuthenticationResponse processOAuth2Authentication(OAuth2UserInfo oauth2UserInfo);
    String getLogoutUrl(String provider);
}
