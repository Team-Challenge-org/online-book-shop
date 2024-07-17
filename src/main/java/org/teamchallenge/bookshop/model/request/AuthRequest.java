package org.teamchallenge.bookshop.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.teamchallenge.bookshop.constants.ValidationConstants.PASSWORD_REGEXP;
import static org.teamchallenge.bookshop.constants.ValidationConstants.WRONG_PASSWORD_CREATION_MESSAGE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String emailOrPhone;
    @Pattern(regexp = PASSWORD_REGEXP, message = WRONG_PASSWORD_CREATION_MESSAGE)
    private String password;
}