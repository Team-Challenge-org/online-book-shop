package org.teamchallenge.bookshop.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.exception.ErrorObject;
import org.teamchallenge.bookshop.exception.UserNotFoundException;

import java.util.Date;

import static org.teamchallenge.bookshop.constants.ValidationConstants.*;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    private ErrorObject createErrorObject(String message, HttpStatus status) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(status.value());
        errorObject.setMessage(message);
        errorObject.setTimestamp(new Date());
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

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorObject> badRequestHandler(IllegalStateException e) {
        return handleException(e, BAD_REQUEST_ERROR_OCCURRED, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorObject> bookNotFound(BookNotFoundException  e) {
        return handleException(e, BOOK_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}