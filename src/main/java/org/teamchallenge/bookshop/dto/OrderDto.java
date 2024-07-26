package org.teamchallenge.bookshop.dto;


import org.teamchallenge.bookshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Map;

public record OrderDto(
        Map<Long, Integer> books,
        OrderStatus status,
        BigDecimal total
) {


}
