package org.teamchallenge.bookshop.exception;

public class StripeInputException extends RuntimeException {
    public StripeInputException(String message) {
        super(message);
    }
}
