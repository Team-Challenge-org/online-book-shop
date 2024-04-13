package org.teamchallenge.bookshop.exception.controller;
import org.teamchallenge.bookshop.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> notFoundHandler(NotFoundException e) {
        return returnResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

     ResponseEntity<String> returnResponse(String message, HttpStatus status) {
        log.error(message);
        return ResponseEntity.status(status).body(message);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> badRequestHandler(IllegalStateException e) {
        return returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
