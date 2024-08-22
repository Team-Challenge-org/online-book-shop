package org.teamchallenge.bookshop.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PasswordResetDto {
    private String oldPassword;
    private String newPassword;
}
