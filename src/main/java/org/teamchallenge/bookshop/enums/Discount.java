package org.teamchallenge.bookshop.enums;

import lombok.Getter;

@Getter
public enum Discount {
    NONE(0),
    FIVE_PERCENT(5),
    TEN_PERCENT(10),
    FIFTEEN_PERCENT(15);

    private final int percentage;

    Discount(int percentage) {
        this.percentage = percentage;
    }

}
