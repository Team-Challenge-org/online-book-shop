package org.teamchallenge.bookshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.teamchallenge.bookshop.dto.BookDto;

import java.util.List;

public interface BookService {

    void addBook(BookDto book);

    BookDto getBookById(Long id);

    BookDto updateBook(BookDto bookDto);

    void deleteBook(Long id);

    List<BookDto> getAllBooks();

    BookDto findBooksByTitle(String title);

    List<BookDto> getSorted(String category, String timeAdded, String price, String author, Float priceMin, Float priceMax);
}
