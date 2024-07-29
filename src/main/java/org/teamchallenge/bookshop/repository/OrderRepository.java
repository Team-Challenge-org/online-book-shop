package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.model.Payment;
import org.teamchallenge.bookshop.model.User;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Optional<Order> findByStripePaymentIntentId(String stripePaymentIntentId);

    Optional<Order> findByPayment(Payment payment);
}
