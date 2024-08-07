package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class InvalidPassword extends RuntimeException {
    public InvalidPassword() {
        super(ValidationConstants.INVALID_PASSWORD);
    }
}
