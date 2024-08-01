package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.dto.OrderDto;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.exception.OrderIdNotFoundException;
import org.teamchallenge.bookshop.mapper.OrderMapper;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Order;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.repository.OrderRepository;
import org.teamchallenge.bookshop.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderIdNotFoundException::new);
        return orderMapper.toOrderDto(order);
    }


    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow(OrderIdNotFoundException::new);
        order.setStatus(orderDto.status());
        order.setStatusChange(LocalDateTime.now().withSecond(0).withNano(0));

        return getOrderDto(orderDto, order);
    }

    private OrderDto getOrderDto(OrderDto orderDto, Order order) {
        Map<Book, Integer> books = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : orderDto.books().entrySet()) {
            Book book = bookRepository.findById(entry.getKey()).orElseThrow(BookNotFoundException::new);
            books.put(book, entry.getValue());

            BigDecimal bookPrice = book.getPrice();
            int quantity = entry.getValue();
            totalAmount = totalAmount.add(bookPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        order.setBooks(books);
        order.setTotal(totalAmount);
        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }


    @Override
    public void deleteOrder(Long id) {
        orderRepository.findById(id).orElseThrow(OrderIdNotFoundException::new);
        orderRepository.deleteById(id);
    }

    @Override
    public void createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setStatus(orderDto.status());
        order.setStatusChange(LocalDateTime.now());

        getOrderDto(orderDto, order);
    }
}
