package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private String category;
    @CreationTimestamp
    private LocalDate timeAdded;
    private String authors;
    @Column(columnDefinition = "TEXT")
    private String titleImage;
    @Column(columnDefinition = "TEXT")
    @ElementCollection
    private List<String> images;
}
