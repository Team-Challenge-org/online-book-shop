package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class CartIdNotFoundException extends RuntimeException {
    public CartIdNotFoundException() {
        super(ValidationConstants.CART_ID_NOT_FOUND);
    }
}