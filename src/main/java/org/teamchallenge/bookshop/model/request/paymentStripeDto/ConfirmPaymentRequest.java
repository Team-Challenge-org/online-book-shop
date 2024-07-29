package org.teamchallenge.bookshop.model.request.paymentStripeDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmPaymentRequest {
    private String paymentIntentId;
}