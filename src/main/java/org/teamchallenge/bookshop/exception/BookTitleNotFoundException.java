package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class BookTitleNotFoundException extends RuntimeException{
    public BookTitleNotFoundException() {
        super(ValidationConstants.BOOK_TITLE_WRONG);
    }
}
