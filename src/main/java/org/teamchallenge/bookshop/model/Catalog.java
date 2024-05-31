package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.teamchallenge.bookshop.enums.Category;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Category name;

}
