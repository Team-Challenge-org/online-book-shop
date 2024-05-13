package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;
    @ManyToMany (cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinTable(
            name = "authors_books",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books;
}
