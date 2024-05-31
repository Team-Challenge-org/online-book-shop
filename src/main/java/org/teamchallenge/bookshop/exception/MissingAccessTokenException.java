package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class MissingAccessTokenException extends RuntimeException {
    public MissingAccessTokenException( ) {
        super(ValidationConstants.MISSING_DROPBOX_ACCESS_TOKEN);
    }
}
