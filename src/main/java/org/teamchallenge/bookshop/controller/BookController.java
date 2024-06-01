package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.dto.BookInCatalogDto;
import org.teamchallenge.bookshop.dto.CategoryDto;
import org.teamchallenge.bookshop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("api/v1/book")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
@Slf4j
@Tag(name = "Book controller", description = "API for book management")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Add a new book")
    //TODO: add @Preauthorize
    @PostMapping("/add")
    public ResponseEntity<Void> addBook(@RequestBody BookDto bookDto ) {
        bookService.addBook(bookDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Get book by id", description = "Get an existing book by id")
    @GetMapping("/findById/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }
    @GetMapping("/category/all")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        return ResponseEntity.ok(bookService.getAllCategory());
    }

    @Operation(summary = "Get all books")
    @GetMapping("/all")
    public ResponseEntity<Page<BookDto>> getAllBooks(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @Operation(summary = "Find book by title", description = "Get an existing book by title")
    @GetMapping("/findByTitle/{title}")
    public ResponseEntity<BookInCatalogDto> getBookByTitle(@PathVariable String title) {
        BookInCatalogDto bookDto = bookService.getBookByTitle(title);
        return ResponseEntity.ok(bookDto);
    }

    @Operation(summary = "Filter books")
    @GetMapping("/filter")
    public ResponseEntity<Page<BookDto>> getFilteredBooks(
         @PageableDefault Pageable pageable,
         @Parameter(description = "Category name")
         @RequestParam(required = false) String  category ,
         @Parameter(description = "Sort by creating time (ASC/DESC)")
         @RequestParam(required = false) String time_added,
         @Parameter(description = "Sort by price (ASC/DESC)")
         @RequestParam(required = false) String price,
         @Parameter(description = "Name of author to search")
         @RequestParam(required = false) String author,
         @Parameter(description = "Minimum of price value")
         @RequestParam(required = false) Float price_min,
         @Parameter(description = "Maximum of price value")
         @RequestParam(required = false) Float price_max
    ) {
        Page<BookDto> bookDtos = bookService.getSorted(pageable, category, time_added, price, author, price_min, price_max);
        return ResponseEntity.ok(bookDtos);
    }

    @Operation(summary = "Get books for slider")
    @GetMapping("/slider")
    public ResponseEntity<List<BookInCatalogDto>> getBooksForSlider() {
        return ResponseEntity.ok(bookService.getBooksForSlider());
    }
}
