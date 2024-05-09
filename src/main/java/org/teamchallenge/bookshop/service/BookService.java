package org.teamchallenge.bookshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.teamchallenge.bookshop.dto.BookDto;

public interface BookService {

    void addBook(BookDto book);

    BookDto getBookById(Long id);

    BookDto updateBook(BookDto bookDto);

    void deleteBook(Long id);

    Page<BookDto> getAllBooks(Pageable pageable);

    BookDto findBooksByTitle(String title);
}
