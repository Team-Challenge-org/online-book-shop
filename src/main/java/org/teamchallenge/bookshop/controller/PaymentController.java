package org.teamchallenge.bookshop.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.enums.OrderStatus;
import org.teamchallenge.bookshop.exception.OrderIdNotFoundException;
import org.teamchallenge.bookshop.exception.PaymentNotFoundException;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.model.Payment;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentResponse;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentResponse;
import org.teamchallenge.bookshop.repository.OrderRepository;
import org.teamchallenge.bookshop.repository.PaymentRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.service.PaymentService;
import org.teamchallenge.bookshop.service.StripeService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
@Slf4j
public class PaymentController {
    private final StripeService stripeService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    @PostMapping("/create-payment-intent")
    public ResponseEntity<CreatePaymentResponse> createPaymentIntent(@RequestBody CreatePaymentRequest request,
                                                                     @AuthenticationPrincipal OAuth2User principal) {
        User user = null;
        if (principal != null) {
            String email = principal.getAttribute("email");
            user = userRepository.findByEmail(email).orElse(null);
        }

        try {
            Payment payment = paymentService.processPayment(request.getAmount(), request.getCurrency());
            paymentRepository.save(payment);

            Order order = new Order();
            order.setTotal(request.getAmount());
            order.setUser(user);
            order.setPayment(payment);
            order.setStripePaymentIntentId(payment.getStripePaymentIntentId());
            orderRepository.save(order);

            log.info("Created payment intent with ID: " + payment.getStripePaymentIntentId());

            return ResponseEntity.ok(new CreatePaymentResponse(payment.getStripePaymentIntentId()));
        } catch (StripeException e) {
            log.error("Error creating payment intent", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<ConfirmPaymentResponse> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        log.info("Confirming payment with intent ID: " + request.getPaymentIntentId());

        try {
            Payment payment = paymentRepository.findByStripePaymentIntentId(request.getPaymentIntentId())
                    .orElseThrow(() -> {
                        log.error("Payment not found for intent ID: " + request.getPaymentIntentId());
                        return new PaymentNotFoundException();
                    });

            PaymentIntent paymentIntent = stripeService.confirmPayment(request.getPaymentIntentId());

            if ("succeeded".equals(paymentIntent.getStatus())) {
                payment.setStatus("COMPLETED");
                paymentRepository.save(payment);

                Order order = orderRepository.findByStripePaymentIntentId(request.getPaymentIntentId())
                        .orElseThrow(() -> {
                            log.error("Order not found for intent ID: " + request.getPaymentIntentId());
                            return new OrderIdNotFoundException();
                        });
                order.setStatus(OrderStatus.PAID);
                order.setStatusChange(LocalDateTime.now());
                orderRepository.save(order);

                log.info("Payment confirmed successfully for intent ID: " + request.getPaymentIntentId());
            }

            return ResponseEntity.ok(new ConfirmPaymentResponse(paymentIntent.getStatus()));
        } catch (StripeException e) {
            log.error("Error confirming payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/order-history")
    public ResponseEntity<List<Order>> getOrderHistory(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        List<Order> orders = orderRepository.findByUser(user);
        return ResponseEntity.ok(orders);
    }

}