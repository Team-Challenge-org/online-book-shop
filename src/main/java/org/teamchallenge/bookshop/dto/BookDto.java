package org.teamchallenge.bookshop.dto;


import lombok.*;
import org.teamchallenge.bookshop.enums.Available;
import org.teamchallenge.bookshop.enums.Category;

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
    private String full_description;
    private String short_description;
    private BigDecimal price;
    private Category category;
    private Boolean isThisNotSlider;
    private Available available;
    private String titleImage;
    private String authors;
    private LocalDate timeAdded;
    private List<String> images;
}