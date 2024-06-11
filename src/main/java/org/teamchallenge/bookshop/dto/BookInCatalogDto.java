package org.teamchallenge.bookshop.dto;

import java.math.BigDecimal;

public record BookInCatalogDto(
        long id,
        String title,
        BigDecimal price,
        String category,
        Boolean isThisSlider,
        String titleImage,
        String authors,
        int totalQuantity
) {}
