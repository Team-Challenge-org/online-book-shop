package org.teamchallenge.bookshop.service.Impl;

import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public Book createBook(Book book) {
        // TODO
        return null;
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        // TODO
        return null;
    }

    @Override
    public Book updateBook(Book book) {
        // TODO
        return null;
    }

    @Override
    public void deleteBook(Long id) {
        // TODO
    }

    @Override
    public List<Book> getAllBooks() {
        // TODO
        return null;
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        // TODO
        return null;
    }
}
