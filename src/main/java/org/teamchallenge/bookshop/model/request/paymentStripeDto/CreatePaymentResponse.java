package org.teamchallenge.bookshop.model.request.paymentStripeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePaymentResponse {
    private String clientSecret;

}
