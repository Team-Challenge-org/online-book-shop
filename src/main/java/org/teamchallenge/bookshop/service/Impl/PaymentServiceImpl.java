package org.teamchallenge.bookshop.service.Impl;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.enums.OrderStatus;
import org.teamchallenge.bookshop.exception.OrderIdNotFoundException;
import org.teamchallenge.bookshop.exception.PaymentNotFoundException;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.model.Payment;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentResponse;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentResponse;
import org.teamchallenge.bookshop.repository.OrderRepository;
import org.teamchallenge.bookshop.repository.PaymentRepository;
import org.teamchallenge.bookshop.service.PaymentService;
import org.teamchallenge.bookshop.service.StripeService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;
    private final OrderRepository orderRepository;

    @Override
    public Payment processPayment(BigDecimal amount, String currency, String paymentMethodId) throws StripeException {
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency, paymentMethodId);
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus("PENDING");
        payment.setTimestamp(LocalDateTime.now());
        payment.setStripePaymentIntentId(paymentIntent.getId());
        return paymentRepository.save(payment);
    }

    @Override
    public CreatePaymentResponse createPaymentIntent(CreatePaymentRequest request, User user) throws StripeException {
        Payment payment = processPayment(request.getAmount(), request.getCurrency(), request.getPaymentMethodId());
        Order order = new Order();
        order.setTotal(request.getAmount());
        order.setUser(user);
        order.setPayment(payment);
        order.setStripePaymentIntentId(payment.getStripePaymentIntentId());
        orderRepository.save(order);

        return new CreatePaymentResponse(payment.getStripePaymentIntentId());
    }

    @Override
    public ConfirmPaymentResponse confirmPayment(ConfirmPaymentRequest request) throws StripeException {
        Payment payment = paymentRepository.findByStripePaymentIntentId(request.getPaymentIntentId())
                .orElseThrow(PaymentNotFoundException::new);

        PaymentIntent paymentIntent = stripeService.confirmPayment(request.getPaymentIntentId());

        if ("succeeded".equals(paymentIntent.getStatus())) {
            payment.setStatus("COMPLETED");
            paymentRepository.save(payment);

            Order order = orderRepository.findByStripePaymentIntentId(request.getPaymentIntentId())
                    .orElseThrow(OrderIdNotFoundException::new);
            order.setStatus(OrderStatus.PAID);
            order.setStatusChange(LocalDateTime.now());
            orderRepository.save(order);
        }

        return new ConfirmPaymentResponse(paymentIntent.getStatus());
    }

    @Override
    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUser(user);
    }
}
