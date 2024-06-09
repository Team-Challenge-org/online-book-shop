package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;


public class DropBoxException extends RuntimeException {
    public DropBoxException() {
        super(ValidationConstants.ERROR_WITH_DROPBOX_COMMUNICATION);
    }
}
