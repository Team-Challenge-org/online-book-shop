package org.teamchallenge.bookshop.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.teamchallenge.bookshop.exception.*;
import org.teamchallenge.bookshop.util.DateUtil;

import java.time.LocalDateTime;

import static org.teamchallenge.bookshop.constants.ValidationConstants.*;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    private ErrorObject createErrorObject(String message, HttpStatus status) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(status.value());
        errorObject.setMessage(message);
        errorObject.setTimestamp(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        return errorObject;
    }

    private ResponseEntity<ErrorObject> handleException(Exception e, String logMessage, HttpStatus status) {
        log.error(logMessage, e);
        ErrorObject errorObject = createErrorObject(e.getMessage(), status);
        return new ResponseEntity<>(errorObject, status);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> notFoundHandler(UserNotFoundException e) {
        return handleException(e, NOT_FOUND_ERROR_OCCURRED, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DropboxFolderCreationException.class)
    public ResponseEntity<ErrorObject> dropboxFolderNotFound(DropboxFolderCreationException e) {
        return handleException(e, DROPBOX_FOLDER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorObject> badRequestHandler(IllegalStateException e) {
        return handleException(e, BAD_REQUEST_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorObject> bookNotFoundHandler(BookNotFoundException e) {
        return handleException(e, BOOK_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BookTitleNotFoundException.class)
    public ResponseEntity<ErrorObject> bookTitleWrong(BookTitleNotFoundException e) {
        return handleException(e, BOOK_TITLE_WRONG, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorObject> userAlreadyExistsHandler(UserAlreadyExistsException e) {
        return handleException(e, USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ErrorObject> userNotAuthenticatedHandler(UserNotAuthenticatedException e) {
        return handleException(e, USER_NOT_AUTHENTICATED, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(WrongEnumConstantException.class)
    public ResponseEntity<ErrorObject> wrongEnumConstant(WrongEnumConstantException e) {
        return handleException(e, WRONG_ENUM_CONSTANT, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DropBoxException.class)
    public ResponseEntity<ErrorObject> problemWithDropBox(DropBoxException e) {
        return handleException(e, ERROR_WITH_DROPBOX_COMMUNICATION, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(CartIdNotFoundException.class)
    public ResponseEntity<ErrorObject> cartIdNotFound(CartIdNotFoundException e) {
        return handleException(e, ERROR_WITH_DROPBOX_COMMUNICATION, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OrderIdNotFoundException.class)
    public ResponseEntity<ErrorObject> orderIdNotFound(OrderIdNotFoundException e) {
        return handleException(e, ERROR_WITH_DROPBOX_COMMUNICATION, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorObject> imageUploadExceptionHandler(ImageUploadException e) {
        return handleException(e, DROPBOX_IMAGE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MissingAccessTokenException.class)
    public ResponseEntity<ErrorObject> missingAccessToken(MissingAccessTokenException e) {
        return handleException(e, MISSING_DROPBOX_ACCESS_TOKEN, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AccessTokenRefreshException.class)
    public ResponseEntity<ErrorObject> missingRefreshAccessToken(AccessTokenRefreshException e) {
        return handleException(e, MISSING_REFRESH_ACCESS_TOKEN, HttpStatus.NOT_FOUND);
    }
}