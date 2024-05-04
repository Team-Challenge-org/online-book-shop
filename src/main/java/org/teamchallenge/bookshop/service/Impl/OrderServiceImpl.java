package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.repository.OrderRepository;
import org.teamchallenge.bookshop.service.OrderService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Optional<Order> getOrderById(Long id) {
        return Optional.of(orderRepository.findById(id)).orElseThrow(NotFoundException::new);
    }

    @Override
    public Order updateOrder(Long id, String status, LocalDateTime time) {
        Order order = orderRepository.findById(id).orElseThrow(NotFoundException::new);
        order.setStatus(status);
        order.setStatusChange(time);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.findById(id).orElseThrow(NotFoundException::new);
        orderRepository.deleteById(id);
    }
}
