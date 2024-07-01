package org.teamchallenge.bookshop.exception;

public class TokenAlreadyBlacklistedException extends RuntimeException {
    public TokenAlreadyBlacklistedException() {
        super("This token was already blacklisted!");
    }
}
