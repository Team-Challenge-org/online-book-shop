package org.teamchallenge.bookshop.dto;



import lombok.*;
import org.teamchallenge.bookshop.model.Author;

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
    private String imageUrl;
    private LocalDate timeAdded;
    private List<Author> authors;
    private List<String> images;
}