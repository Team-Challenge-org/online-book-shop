package org.teamchallenge.bookshop.exception;
import org.teamchallenge.bookshop.constants.ValidationConstants;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(ValidationConstants.USER_NOT_FOUND);
    }
}
