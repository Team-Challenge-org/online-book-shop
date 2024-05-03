package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.model.Book;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface BookService {

    Book createBook(Book book);

    Optional<Book> getBookById(Long id);

    Book updateBook(Book book);

    void deleteBook(Long id);

    List<Book> getAllBooks(Pageable pageable);

    Optional<Book> findBooksByTitle(String title);
}
