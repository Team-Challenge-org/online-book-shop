package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super(ValidationConstants.PAYMENT_NOT_FOUND);
    }
}
