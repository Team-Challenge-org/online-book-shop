package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.model.Order;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderService {
    Optional<Order> getOrderById(Long id);

    Order updateOrder(Long id, String status, LocalDateTime time);

    void deleteOrder(Long id);
}
