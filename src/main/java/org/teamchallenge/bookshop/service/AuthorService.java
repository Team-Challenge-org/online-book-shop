package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.model.Author;

import java.util.Optional;

public interface AuthorService {
    void createAuthor(Author author);

    Optional<Author> findById(Long id);

    Author updateAuthor(Author author);

    void deleteAuthor(Long id);
}
