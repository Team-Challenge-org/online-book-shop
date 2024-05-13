package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.model.Author;
import org.teamchallenge.bookshop.repository.AuthorRepository;
import org.teamchallenge.bookshop.service.AuthorService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public void createAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.of(authorRepository.findById(id)).orElseThrow(NotFoundException::new);
    }

    @Override
    public Author updateAuthor(Author author) {
        authorRepository.findById(author.getId()).orElseThrow(NotFoundException::new);
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.findById(id).orElseThrow(NotFoundException::new);
        authorRepository.deleteById(id);
    }
}
