package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class DropboxDeleteException extends RuntimeException {
    public DropboxDeleteException(String message) {
        super(message + " " + ValidationConstants.DROPBOX_DELETE_FOLDER);
    }
}