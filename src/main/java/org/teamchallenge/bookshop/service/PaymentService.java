package org.teamchallenge.bookshop.service;

import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.model.Payment;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentResponse;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentResponse;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    Payment processPayment(BigDecimal amount, String currency, String paymentMethodId) throws StripeException;

    @Transactional
    CreatePaymentResponse createPaymentIntent(CreatePaymentRequest request, User user) throws StripeException;
    @Transactional
    ConfirmPaymentResponse confirmPayment(ConfirmPaymentRequest request) throws StripeException;

    List<Order> getOrderHistory(User user);
}
