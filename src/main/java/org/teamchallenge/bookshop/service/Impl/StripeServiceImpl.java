package org.teamchallenge.bookshop.service.Impl;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.service.StripeService;

import java.math.BigDecimal;

@Service
public class StripeServiceImpl implements StripeService {

    @Override
    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency, String paymentMethodId) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(new BigDecimal(100)).longValue())
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setConfirm(true)
                .build();

        return PaymentIntent.create(params);
    }

    @Override
    public PaymentIntent confirmPayment(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.confirm();
    }
}