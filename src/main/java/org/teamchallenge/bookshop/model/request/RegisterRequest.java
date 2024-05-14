package org.teamchallenge.bookshop.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.teamchallenge.bookshop.constants.ValidationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String surname;
    @Pattern(regexp = EMAIL_REGEXP, message = WRONG_EMAIL_FORMAT)
    private String email;
    @Pattern(regexp = PASSWORD_REGEXP, message = WRONG_PASSWORD_CREATION_MESSAGE)
    private String password;
}
