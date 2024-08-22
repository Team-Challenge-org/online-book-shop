package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message) {
        super(ValidationConstants.OLD_PASSWORD_INCORRECT);
    }
}
