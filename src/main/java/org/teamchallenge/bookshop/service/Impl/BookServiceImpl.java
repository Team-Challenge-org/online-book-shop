package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    // TODO: Use DTO instead of model
    @Override
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return Optional.of(bookRepository.findById(id)).orElseThrow(NotFoundException::new);
    }

    @Override
    public Book updateBook(Book book) {
        bookRepository.findById(book.getId()).orElseThrow(NotFoundException::new);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.findById(id).orElseThrow(NotFoundException::new);
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks(Pageable pageable) {;
        return bookRepository.findAll(pageable).stream().toList();
    }

    @Override
    public Optional<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}
