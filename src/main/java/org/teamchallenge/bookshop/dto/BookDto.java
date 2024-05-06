package org.teamchallenge.bookshop.dto;


import java.math.BigDecimal;

public record BookDto(
         long id,
         String title,
         String description,
         BigDecimal price,
         String category,
         String imageUrl
) {


}
