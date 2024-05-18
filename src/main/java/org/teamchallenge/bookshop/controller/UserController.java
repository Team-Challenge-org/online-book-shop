package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Add book to favourites")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/favourites/add")
    public ResponseEntity<Void> addBookToFavourites(@RequestParam Long id) {
        userService.addBookToFavourites(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Remove book to favourites")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/favourites/delete")
    public ResponseEntity<Void> deleteBookFromFavourites(@RequestParam Long id) {
        userService.deleteBookFromFavourites(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Show all books in favourites")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/favourites")
    public ResponseEntity<List<BookDto>> getUserFavourites() {
        return ResponseEntity.ok(userService.getFavouriteBooks());
    }
}