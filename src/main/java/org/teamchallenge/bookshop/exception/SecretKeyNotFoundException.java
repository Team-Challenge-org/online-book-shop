package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class SecretKeyNotFoundException extends RuntimeException{
    public SecretKeyNotFoundException() {
        super(ValidationConstants.SECRET_KEY_NOT_FOUND);
    }
}