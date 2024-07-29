package org.teamchallenge.bookshop.service.Impl;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;
import com.stripe.param.PaymentMethodCreateParams;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.service.StripeService;

import java.math.BigDecimal;

@Service
public class StripeServiceImpl implements StripeService {

    @Override
    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency) throws StripeException {
        PaymentMethodCreateParams params = PaymentMethodCreateParams.builder()
                .setType(PaymentMethodCreateParams.Type.CARD)
                .setCard(PaymentMethodCreateParams.CardDetails.builder()
                        .setNumber("4242424242424242")
                        .setExpMonth(12L)
                        .setExpYear(2024L)
                        .setCvc("123")
                        .build())
                .build();

        PaymentMethod paymentMethod = PaymentMethod.create(params);
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(new BigDecimal(100)).longValue())
                .setCurrency(currency)
                .setPaymentMethod(paymentMethod.getId())
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .setConfirm(true)
                .build();

        return PaymentIntent.create(paymentIntentParams);
    }
@Override
    public PaymentIntent confirmPayment(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.confirm();
    }
}