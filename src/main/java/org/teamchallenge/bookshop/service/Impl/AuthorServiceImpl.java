package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.AuthorMapper;
import org.teamchallenge.bookshop.dto.AuthorDto;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.model.Author;
import org.teamchallenge.bookshop.repository.AuthorRepository;
import org.teamchallenge.bookshop.service.AuthorService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public void createAuthor(AuthorDto author) {
        authorRepository.save(authorMapper.dtoToEntity(author));
    }

    @Override
    public Optional<AuthorDto> findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(NotFoundException::new);
        return Optional.of(authorMapper.entityToDTO(author));
    }

    @Override
    public AuthorDto updateAuthor(AuthorDto author) {
        Author auth = authorRepository.findById(author.getId()).orElseThrow(NotFoundException::new);
        Author newAuthor = authorMapper.dtoToEntity(author);
        return authorMapper.entityToDTO(authorRepository.save(newAuthor));
    }

    @Override
    public void deleteAuthor(Long id) {
        authorRepository.findById(id).orElseThrow(NotFoundException::new);
        authorRepository.deleteById(id);
    }
}
