package org.teamchallenge.bookshop.dto;



import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private LocalDate timeAdded;
}