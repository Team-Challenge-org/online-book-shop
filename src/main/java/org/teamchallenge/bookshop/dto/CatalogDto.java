package org.teamchallenge.bookshop.dto;

import org.teamchallenge.bookshop.enums.Category;

import java.math.BigDecimal;

public record CatalogDto(long id,
                         BigDecimal price,
                         Category category) {
}
