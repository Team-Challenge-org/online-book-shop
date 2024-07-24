package org.teamchallenge.bookshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2UserInfo {
    private String name;
    private String surname;
    private String email;
    private String provider;
    private String providerId;
}
