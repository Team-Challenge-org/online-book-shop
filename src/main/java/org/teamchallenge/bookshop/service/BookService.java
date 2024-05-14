package org.teamchallenge.bookshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.enums.Category;

import java.util.List;

public interface BookService {

    void addBook(BookDto book);

    BookDto getBookById(Long id);

    BookDto updateBook(BookDto bookDto);

    void deleteBook(Long id);

    List<BookDto> getAllBooks();

    BookInCatalogDto findBooksByTitle(String title);

    Page<BookInCatalogDto> getFiveBooksForSlider(Pageable paging);

    Page<BookDto> getBookByTimeAdded(Pageable paging);

    List<BookDto> getSorted(Category category, String timeAdded, String price, String author, Float priceMin, Float priceMax);

}
