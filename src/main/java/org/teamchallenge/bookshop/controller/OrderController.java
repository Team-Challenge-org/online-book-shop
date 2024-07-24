package org.teamchallenge.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.OrderDto;
import org.teamchallenge.bookshop.enums.OrderStatus;
import org.teamchallenge.bookshop.service.OrderService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto) {
       orderService.createOrder(orderDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/statuses")
    public ResponseEntity<List<OrderStatus>> getOrderStatuses() {
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.values());
        return ResponseEntity.ok(orderStatuses);
    }
    @GetMapping("/findById/{id}")
    public Optional<OrderDto> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PutMapping("/update/{id}")
    public OrderDto updateOrderById(@PathVariable Long id, @RequestBody OrderDto orderDto) {

        return orderService.updateOrder(id, orderDto);
    }
}
