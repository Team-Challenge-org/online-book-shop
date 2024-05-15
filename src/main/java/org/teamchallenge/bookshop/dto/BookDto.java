package org.teamchallenge.bookshop.dto;



import lombok.*;
import org.teamchallenge.bookshop.enums.Available;
import org.teamchallenge.bookshop.enums.Category;
import org.teamchallenge.bookshop.model.Author;

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
    private String full_description;
    private String short_description;
    private BigDecimal price;
    private Category category;
    private Available available;
    private String imageUrl;
    private String author;
}