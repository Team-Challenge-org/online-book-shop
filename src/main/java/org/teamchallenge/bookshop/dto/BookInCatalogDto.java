package org.teamchallenge.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.teamchallenge.bookshop.enums.Available;
import org.teamchallenge.bookshop.enums.Category;

import java.math.BigDecimal;

public record BookInCatalogDto(
        long id,
        String title,
        BigDecimal price,
        String category,
        Boolean isThisSlider,
        Available available,
        String titleImage,
        String authors
) {}
