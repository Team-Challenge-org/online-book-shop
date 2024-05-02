package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ElementCollection
    @MapKeyJoinColumn(name = "books.id")
    @Column(name = "count")
    private Map<Book, Integer> books = new HashMap<>();
    @NonNull
    private String status;
    @NonNull
    private LocalDateTime statusChange;
}
