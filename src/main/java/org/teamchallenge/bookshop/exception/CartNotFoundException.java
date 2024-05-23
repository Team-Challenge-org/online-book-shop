package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super(ValidationConstants.CART_NOT_FOUND);
    }
}