package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.OrderDto;

public interface OrderService {
    OrderDto getOrderById(Long id);

    OrderDto updateOrder(Long id, OrderDto orderDto);

    void deleteOrder(Long id);

    void createOrder(OrderDto orderDto);
}
