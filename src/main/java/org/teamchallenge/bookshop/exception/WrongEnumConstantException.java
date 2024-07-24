package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class WrongEnumConstantException extends RuntimeException {
    public WrongEnumConstantException(String message) {
        super(ValidationConstants.WRONG_ENUM_CONSTANT + message);
    }
}
