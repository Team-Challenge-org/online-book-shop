package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.dto.CreateBookDto;
import org.teamchallenge.bookshop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("api/v1/book")
@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookService bookService;

    @Operation(description = "Add book")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Void> addBook(@RequestBody CreateBookDto bookDto ) {
        bookService.addBook(bookDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(description = "Find book by Id")
    @GetMapping("/findById/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }

    @Operation(description = "Find all books")
    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @GetMapping("/findByTitle/{title}")
    public ResponseEntity<BookInCatalogDto> getBookByTitle(@PathVariable String title) {
        BookInCatalogDto bookDto = bookService.getBookByTitle(title);
        return ResponseEntity.ok(bookDto);
    }

    @Operation(description = "Find filtered books by params")
    @GetMapping("/filter")
    public ResponseEntity<List<BookDto>> getFilteredBooks(@RequestParam (required = false) String category ,
                                                          @RequestParam (required = false) String time_added,
                                                          @RequestParam (required = false) String price,
                                                          @RequestParam (required = false) String author,
                                                          @RequestParam (required = false) Float price_min,
                                                          @RequestParam (required = false) Float price_max) {
        List<BookDto> bookDtos = bookService.getSorted(category, time_added, price, author, price_min, price_max);
        return ResponseEntity.ok(bookDtos);
    }

    @Operation(description = "Find few books for slider")
    @GetMapping("/slider")
    public ResponseEntity<List<BookDto>> getRandomBooks(@RequestParam Integer count) {
        return ResponseEntity.ok(bookService.getRandomByCount(count));
    }

}
