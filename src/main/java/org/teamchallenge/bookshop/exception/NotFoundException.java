package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class NotFoundException  extends RuntimeException{
    public NotFoundException() {
        super(ValidationConstants.NOT_FOUND);
    }
}
