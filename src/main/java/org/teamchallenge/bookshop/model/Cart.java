package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.teamchallenge.bookshop.enums.Discount;

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
    @Enumerated(EnumType.STRING)
    private Discount discount = Discount.NONE;

}
