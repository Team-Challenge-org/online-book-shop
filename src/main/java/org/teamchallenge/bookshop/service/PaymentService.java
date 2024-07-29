package org.teamchallenge.bookshop.service;

import com.stripe.exception.StripeException;
import org.teamchallenge.bookshop.model.Payment;

import java.math.BigDecimal;

public interface PaymentService {
    Payment processPayment(BigDecimal amount, String currency) throws StripeException;
}
