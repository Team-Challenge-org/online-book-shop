package org.teamchallenge.bookshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponseException extends RuntimeException {
    private final HttpStatus status;

    public SuccessResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}