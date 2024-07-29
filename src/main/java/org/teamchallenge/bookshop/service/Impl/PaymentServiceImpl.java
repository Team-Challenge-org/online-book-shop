package org.teamchallenge.bookshop.service.Impl;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.model.Payment;
import org.teamchallenge.bookshop.repository.PaymentRepository;
import org.teamchallenge.bookshop.service.PaymentService;
import org.teamchallenge.bookshop.service.StripeService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    public Payment processPayment(BigDecimal amount, String currency) throws StripeException {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus("PENDING");
        payment.setTimestamp(LocalDateTime.now());

        PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency);
        payment.setStripePaymentIntentId(paymentIntent.getId());

        return paymentRepository.save(payment);
    }
}