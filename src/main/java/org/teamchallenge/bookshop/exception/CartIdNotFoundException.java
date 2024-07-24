package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class CartIdNotFoundException extends RuntimeException {
    public CartIdNotFoundException() {
        super(ValidationConstants.CART_NOT_FOUND);
    }
}