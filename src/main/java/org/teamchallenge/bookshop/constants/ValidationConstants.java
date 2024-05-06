package org.teamchallenge.bookshop.constants;

public interface ValidationConstants {
    String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[A-Z]).{6,15}$";
    String EMAIL_REGEXP = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    String USER_NOT_FOUND = "User not found";
    String BOOK_NOT_FOUND = "Book not found";
    String NOT_FOUND_ERROR_OCCURRED = "Not found error occurred";
    String BAD_REQUEST_ERROR_OCCURRED = "Bad request error occurred";
    String WRONG_EMAIL_FORMAT = "Wrong email format";
    String NOT_FOUND = "Not found";
    String WRONG_PASSWORD_CREATION_MESSAGE = "The password must consist of at least 6 characters. Contain a number and one capital letter";
}
