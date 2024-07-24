package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;


public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(ValidationConstants.INVALID_RESET_TOKEN);
    }

}
