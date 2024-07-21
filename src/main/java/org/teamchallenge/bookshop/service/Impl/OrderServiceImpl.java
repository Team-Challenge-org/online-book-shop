package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.dto.OrderDto;
import org.teamchallenge.bookshop.enums.OrderStatus;
import org.teamchallenge.bookshop.exception.OrderIdNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.repository.OrderRepository;
import org.teamchallenge.bookshop.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Override
    public Optional<Order> getOrderById(Long id) {
        return Optional.of(orderRepository.findById(id)).orElseThrow(OrderIdNotFoundException::new);
    }

    @Override
    public Order updateOrder(Long id, OrderStatus status, LocalDateTime time) {
        Order order = orderRepository.findById(id).orElseThrow(OrderIdNotFoundException::new);
        order.setStatus(status);
        order.setStatusChange(time);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.findById(id).orElseThrow(OrderIdNotFoundException::new);
        orderRepository.deleteById(id);
    }

    @Override
    public Order createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setStatus(orderDto.status());
        order.setStatusChange(LocalDateTime.now());

        Map<Book, Integer> books = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : orderDto.books().entrySet()) {
            Book book = bookRepository.findById(entry.getKey()).orElseThrow(() -> new RuntimeException("Book not found"));
            books.put(book, entry.getValue());

            BigDecimal bookPrice = book.getPrice();
            int quantity = entry.getValue();
            totalAmount = totalAmount.add(bookPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        order.setBooks(books);
        order.setTotal(totalAmount);

        return orderRepository.save(order);
    }
}
