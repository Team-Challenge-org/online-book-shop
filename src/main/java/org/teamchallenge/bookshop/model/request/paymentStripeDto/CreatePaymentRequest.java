package org.teamchallenge.bookshop.model.request.paymentStripeDto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class CreatePaymentRequest  {
    private String paymentMethodId;
    private BigDecimal amount;
    private String currency;

}
