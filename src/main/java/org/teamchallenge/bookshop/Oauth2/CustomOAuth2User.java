package org.teamchallenge.bookshop.Oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
@Getter
@Setter
@Builder
public class CustomOAuth2User implements OAuth2User {
    private OAuth2User oauth2User;
    private String provider;

    public CustomOAuth2User(OAuth2User oauth2User, String provider) {
        this.oauth2User = oauth2User;
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getProviderId() {
        return oauth2User.getAttribute("sub");
    }
}