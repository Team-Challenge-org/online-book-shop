package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class DropboxFolderCreationException  extends RuntimeException {
    public DropboxFolderCreationException () {
        super(ValidationConstants.DROPBOX_FOLDER_NOT_FOUND);
    }
}
