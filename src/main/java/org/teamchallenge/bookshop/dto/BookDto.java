package org.teamchallenge.bookshop.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private String authors;
    private String titleImage;
    private String imageUrl;
    private LocalDate timeAdded;
    private List<String> images;
}