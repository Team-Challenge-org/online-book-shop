package org.teamchallenge.bookshop.exception;

import lombok.Data;


@Data
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private String timestamp;

}