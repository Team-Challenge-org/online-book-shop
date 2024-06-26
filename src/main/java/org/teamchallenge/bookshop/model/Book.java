package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.teamchallenge.bookshop.enums.Available;
import org.teamchallenge.bookshop.enums.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String title;
    @Column(columnDefinition = "TEXT")
    private String full_description;
    private String short_description;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'IN_STOCK'")
    private Available available;
    private Boolean isThisNotSlider;
    @CreationTimestamp
    private LocalDate timeAdded;
    private String authors;
    private String titleImage;
    @ElementCollection
    private List<String> images;

}
