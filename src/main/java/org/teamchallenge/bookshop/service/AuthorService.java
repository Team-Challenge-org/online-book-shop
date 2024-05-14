package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.AuthorDto;
import org.teamchallenge.bookshop.model.Author;

import java.util.Optional;

public interface AuthorService {
    void createAuthor(AuthorDto author);

    Optional<AuthorDto> findById(Long id);

    AuthorDto updateAuthor(AuthorDto author);

    void deleteAuthor(Long id);
}
