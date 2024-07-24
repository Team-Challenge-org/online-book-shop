package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;


public class AccessTokenRefreshException extends RuntimeException {
    public AccessTokenRefreshException() {
        super(ValidationConstants.MISSING_REFRESH_ACCESS_TOKEN);
    }

}
