package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Boolean isPermanent;
    private LocalDate lastModified;
    @ElementCollection
    @MapKeyJoinColumn(name = "books.id")
    @Column(name = "count")
    private Map<Book, Integer> items = new HashMap<>();

    public void addOrUpdateBook(Book book, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        items.merge(book, quantity, Integer::sum);
    }

    public void deleteBook(Book book) {
        items.remove(book);
    }
}
