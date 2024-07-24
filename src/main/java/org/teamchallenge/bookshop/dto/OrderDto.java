package org.teamchallenge.bookshop.dto;


import org.teamchallenge.bookshop.enums.OrderStatus;
import org.teamchallenge.bookshop.model.Book;

import java.math.BigDecimal;
import java.util.Map;

public record OrderDto(
        Map<Long, Integer> books,
        OrderStatus status,
        BigDecimal total
) {


}
