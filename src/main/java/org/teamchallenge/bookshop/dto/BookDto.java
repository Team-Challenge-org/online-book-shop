package org.teamchallenge.bookshop.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public record BookDto(
         long id,
         String title,
         String description,
         BigDecimal price,
         String category,
         String imageUrl,
         LocalDate timeAdded
) {


}
