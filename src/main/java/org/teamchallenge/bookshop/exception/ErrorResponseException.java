package org.teamchallenge.bookshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseException extends RuntimeException {
    private final HttpStatus status;

    public ErrorResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}