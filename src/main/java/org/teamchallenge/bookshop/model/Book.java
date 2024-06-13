package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
    private Boolean isThisSlider;
    @CreationTimestamp
    private LocalDate timeAdded;
    private String authors;
    private String titleImage;
    @Fetch(FetchMode.JOIN)
    @ElementCollection
    private List<String> images;
    private int quantity;


}
