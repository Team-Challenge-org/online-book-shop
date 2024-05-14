package org.teamchallenge.bookshop.dto;

import lombok.Getter;
import lombok.Setter;
import org.teamchallenge.bookshop.model.Book;

import java.util.List;

@Getter
@Setter
public class AuthorDto {
    private Long id;
    private String fullName;
    private List<Book> books;
}
