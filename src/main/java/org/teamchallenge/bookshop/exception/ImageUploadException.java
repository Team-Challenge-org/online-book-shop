package org.teamchallenge.bookshop.exception;

import static org.teamchallenge.bookshop.constants.ValidationConstants.DROPBOX_FAILED_UPLOAD_IMAGE;
import static org.teamchallenge.bookshop.constants.ValidationConstants.DROPBOX_IMAGE_NOT_FOUND;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException() {
        super(DROPBOX_IMAGE_NOT_FOUND);
    }

    public ImageUploadException(String message) {
        super(DROPBOX_FAILED_UPLOAD_IMAGE + message + " to Dropbox");
    }
}