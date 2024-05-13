package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super(ValidationConstants.USER_ALREADY_EXISTS);
    }
}
