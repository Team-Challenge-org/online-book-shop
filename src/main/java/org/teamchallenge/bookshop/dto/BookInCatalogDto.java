package org.teamchallenge.bookshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@RequiredArgsConstructor
public class BookInCatalogDto {
    private long id;
    private String title;
    private BigDecimal price;
    private String category;
    private Boolean isThisSlider;
    private String titleImage;
    private String authors;
    private final int quantity = 1;
}