package org.teamchallenge.bookshop.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.math.BigDecimal;

public interface StripeService {
    PaymentIntent createPaymentIntent(BigDecimal amount, String currency) throws StripeException;
    PaymentIntent confirmPayment(String paymentIntentId) throws StripeException;
}
