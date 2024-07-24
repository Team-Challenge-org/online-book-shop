package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.OrderDto;
import org.teamchallenge.bookshop.enums.OrderStatus;
import org.teamchallenge.bookshop.model.Order;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderService {
    Optional<Order> getOrderById(Long id);

    Order updateOrder(Long id, OrderStatus status, LocalDateTime time);

    void deleteOrder(Long id);

    Order createOrder(OrderDto orderDto);
}
