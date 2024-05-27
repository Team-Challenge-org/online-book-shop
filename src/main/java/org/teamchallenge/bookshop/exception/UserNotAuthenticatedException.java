package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException() {
    super(ValidationConstants.USER_NOT_AUTHENTICATED);
    }
}
