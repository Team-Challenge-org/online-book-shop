package org.teamchallenge.bookshop.dto;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
        long id,
        @NonNull List<CartItemDto> items,
        BigDecimal total
) {
    public static CartDto create(long id, List<CartItemDto> items, BigDecimal total) {
        return new CartDto(id, items, total);
    }
}
