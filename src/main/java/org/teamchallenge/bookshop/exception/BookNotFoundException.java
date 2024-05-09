package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {

        super(ValidationConstants.BOOK_NOT_FOUND);
    }
}
