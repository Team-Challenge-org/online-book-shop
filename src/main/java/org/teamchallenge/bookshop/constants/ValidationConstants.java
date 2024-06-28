package org.teamchallenge.bookshop.constants;

public interface ValidationConstants {
    String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[A-Z]).{6,15}$";
    String EMAIL_REGEXP = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    String USER_NOT_FOUND = "User not found";
    String USER_ALREADY_EXISTS = "User with such email already exists";
    String BOOK_NOT_FOUND = "Book not found";
    String NOT_FOUND_ERROR_OCCURRED = "Not found error occurred";
    String BAD_REQUEST_ERROR_OCCURRED = "Bad request error occurred";
    String WRONG_EMAIL_FORMAT = "Wrong email format";
    String NOT_FOUND = "Not found";
    String WRONG_PASSWORD_CREATION_MESSAGE = "The password must consist of at least 6 characters. Contain a number and one capital letter";
    String USER_NOT_AUTHENTICATED = "User is not authenticated";
    String SECRET_KEY_NOT_FOUND = "Secret Key in getenv not found";
    String CART_NOT_FOUND = "Cart not found";
    String DROPBOX_IMAGE_NOT_FOUND = "Dropbox image not found";
    String DROPBOX_FOLDER_NOT_FOUND = "Dropbox image not found";
    String DROPBOX_FAILED_UPLOAD_IMAGE = "Failed to upload image " ;
    String MISSING_DROPBOX_ACCESS_TOKEN = " Missing dropbox access token" ;
    String MISSING_REFRESH_ACCESS_TOKEN = "Missing refresh dropbox  access token " ;
    String DROPBOX_INVALID_AUTHORIZATION_VALUE = "Invalid authorization value" ;
    String BOOK_TITLE_WRONG ="Wrong book title";
    String WRONG_ENUM_CONSTANT = "No enum constant with name:  ";
    String ERROR_WITH_DROPBOX_COMMUNICATION = "Error while communicating with Dropbox" ;
    String ORDER_ID_NOT_FOUND = "Order id not found";
    String DROPBOX_DELETE_FOLDER = "Failed to delete folder from Dropbox: ";
}
