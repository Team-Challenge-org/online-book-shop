package org.teamchallenge.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.enums.Category;
import org.teamchallenge.bookshop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("api/v1/book")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class BookController {
    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<Void> addBook(@RequestBody BookDto bookDto) {
        bookService.addBook(bookDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/slider")
    public ResponseEntity<Page<BookInCatalogDto>> getFiveBooksForSlider(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getFiveBooksForSlider(paging));
    }

    @GetMapping("/{findByTitle}")
    public ResponseEntity<BookInCatalogDto> getBookWithRightTitle(@PathVariable String findByTitle) {
        return ResponseEntity.ok(bookService.findBooksByTitle(findByTitle));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<BookDto>> getFilteredBooks(@RequestParam(required = false) Category category,
                                                          @RequestParam(required = false) String time_added,
                                                          @RequestParam(required = false) String price,
                                                          @RequestParam(required = false) String author,
                                                          @RequestParam(required = false) Float price_min,
                                                          @RequestParam(required = false) Float price_max) {
        List<BookDto> bookDtos = bookService.getSorted(category, time_added, price, author, price_min, price_max);
        return ResponseEntity.ok(bookDtos);
    }


    @GetMapping("/timeAdded")
    Page<BookDto> getNewBooksByTimeAdded(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "timeAdded") String sortByTimeAdded,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable paging = PageRequest.of(page, size, sortDirection, sortByTimeAdded);
        return bookService.getBookByTimeAdded(paging);
    }
}
