

package org.teamchallenge.bookshop.exception;

import org.teamchallenge.bookshop.constants.ValidationConstants;

public class OrderIdNotFoundException extends RuntimeException {
    public OrderIdNotFoundException() {
        super(ValidationConstants.ORDER_ID_NOT_FOUND);
    }
}
