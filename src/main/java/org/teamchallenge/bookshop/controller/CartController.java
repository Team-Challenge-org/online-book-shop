package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.service.CartService;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class CartController {
    private final CartService cartService;
    @Operation(summary = "get cart by id")
    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCartById(@PathVariable Long id) {
            return ResponseEntity.ok(cartService.getCartById(id));
    }
    @Operation(summary = "add certain amount of books to authorized user cart and calculate total")
    @PostMapping("/book/{bookId}")
    public ResponseEntity<CartDto> addBookToCart( @PathVariable Long bookId, @RequestParam int amount) {
            return ResponseEntity.ok(cartService.addBookToCart(bookId, amount));
    }
    @Operation(summary = "total money in authorized user cart")
    @GetMapping("/total")
    private ResponseEntity<BigDecimal> getTotalInCart() {
            return ResponseEntity.ok(cartService.getTotalInCart());
    }
    @Operation(summary = "clear all cart")
    @PutMapping("/clear")
    public ResponseEntity<Void> clearCart() {
            cartService.clearCart();
            return ResponseEntity.noContent().build();
    }
}
