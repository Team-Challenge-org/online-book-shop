package org.teamchallenge.bookshop.dto;

import org.teamchallenge.bookshop.enums.Available;
import org.teamchallenge.bookshop.enums.Category;

import java.math.BigDecimal;

public record BookInCatalogDto(
        long id,
        String title,
        BigDecimal price,
        Category category,
        Boolean isThisNotSlider,
        Available available,
        String titleImage,
        String authors
) {}
