package org.teamchallenge.bookshop.exception;
import org.teamchallenge.bookshop.constants.ValidationConstants;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super(ValidationConstants.USER_NOT_FOUND);
    }
}
