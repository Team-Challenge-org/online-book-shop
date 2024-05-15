package org.teamchallenge.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("api/v1/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Void> addBook(@RequestBody BookDto bookDto ) {
        bookService.addBook(bookDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok(bookDto);
    }
//
//    @GetMapping("/filter")
//    public ResponseEntity<List<BookDto>> getFilteredBooks(@RequestParam (required = false) String category ,
//                                                          @RequestParam (required = false) String time_added,
//                                                          @RequestParam (required = false) String price,
//                                                          @RequestParam (required = false) String author,
//                                                          @RequestParam (required = false) Float price_min,
//                                                          @RequestParam (required = false) Float price_max) {
//        List<BookDto> bookDtos = bookService.getSorted(category, time_added, price, author, price_min, price_max);
//        return ResponseEntity.ok(bookDtos);
//    }

    @GetMapping("/slider")
    public ResponseEntity<List<BookDto>> getRandomBooks(@RequestParam Integer count) {
        return ResponseEntity.ok(bookService.getRandomByCount(count));
    }

}
