package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.OrderDto;
import org.teamchallenge.bookshop.enums.OrderStatus;
import org.teamchallenge.bookshop.model.Order;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderService {
    OrderDto getOrderById(Long id);

    OrderDto updateOrder(Long id, OrderDto orderDto);

    void deleteOrder(Long id);

    OrderDto createOrder(OrderDto orderDto);
}
