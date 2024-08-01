package org.teamchallenge.bookshop.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.ConfirmPaymentResponse;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentRequest;
import org.teamchallenge.bookshop.model.request.paymentStripeDto.CreatePaymentResponse;
import org.teamchallenge.bookshop.service.PaymentService;
import org.teamchallenge.bookshop.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@AllArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<CreatePaymentResponse> createPaymentIntent(@RequestBody CreatePaymentRequest request) {
        try {
            User user = userService.getAuthenticatedUser();
            CreatePaymentResponse response = paymentService.createPaymentIntent(request, user);
            log.info("Created payment intent with ID: " + response.getPaymentIntentId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating payment intent", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<ConfirmPaymentResponse> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        try {
            ConfirmPaymentResponse response = paymentService.confirmPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error confirming payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/order-history")
    public ResponseEntity<List<Order>> getOrderHistory(){


        try {
            User user = userService.getAuthenticatedUser();
            List<Order> orders = paymentService.getOrderHistory(user);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Error fetching order history", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
