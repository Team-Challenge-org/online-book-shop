package org.teamchallenge.bookshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.dto.CategoryDto;

import java.util.List;

@Service
public interface BookService {

    void addBook(BookDto book);

    BookDto getBookById(Long id);

    List<BookInCatalogDto> getBooksForSlider();

    List<CategoryDto> getAllCategory();

    BookDto updateBook(BookDto bookDto);

    void deleteBook(Long id);

    Page<BookDto> getAllBooks(Pageable pageable);

    BookInCatalogDto getFirstBookByTitle(String title);

    Page<BookDto> getSorted(Pageable pageable,
                            Integer categoryId,
                            String timeAdded,
                            String price,
                            String author,
                            Float priceMin,
                            Float priceMax);
}
