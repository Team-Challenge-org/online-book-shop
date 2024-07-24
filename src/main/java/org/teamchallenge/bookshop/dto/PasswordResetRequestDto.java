package org.teamchallenge.bookshop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import static org.teamchallenge.bookshop.constants.ValidationConstants.*;


@Getter
@Builder
public class PasswordResetRequestDto {

    @NotEmpty(message =EMPTY_TOKEN )
    private String token;
    @Pattern(regexp = PASSWORD_REGEXP, message = INVALID_PASSWORD)
    private String newPassword;
}
