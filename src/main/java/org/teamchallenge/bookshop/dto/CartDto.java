package org.teamchallenge.bookshop.dto;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
       List<CartItemDto> items,
        BigDecimal total
) {

}
