package org.teamchallenge.bookshop.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private String token;
    private int amount;
}
