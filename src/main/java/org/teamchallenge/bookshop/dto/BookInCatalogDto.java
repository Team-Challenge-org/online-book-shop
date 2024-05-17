package org.teamchallenge.bookshop.dto;

import org.teamchallenge.bookshop.enums.Available;
import org.teamchallenge.bookshop.enums.Category;

import java.math.BigDecimal;

public record BookInCatalogDto(
        long id,
        String title,
        String fullDescription,
        String shortDescription,
        BigDecimal price,
        Category category,
        Available available,
        String imageUrl,
        String authors
) {}
