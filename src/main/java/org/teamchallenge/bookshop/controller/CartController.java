package org.teamchallenge.bookshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.service.CartService;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class CartController {
    private final CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<UUID> createCart() {
        return ResponseEntity.ok(cartService.createCart().getId());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<CartDto> getCartById(@PathVariable UUID id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addBookToCart(@RequestParam UUID cartId,
                                              @RequestParam long bookId) {
        return ResponseEntity.ok(cartService.addBookToCart(cartId, bookId));
    }

    @PostMapping("/update")
    public ResponseEntity<CartDto> updateCart(@RequestParam UUID cartId,
                                           @RequestParam long bookId,
                                           @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(cartId, bookId, quantity));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CartDto> deleteBookFromCart(@RequestParam UUID cartId,
                                                   @RequestParam long bookId) {
        return ResponseEntity.ok(cartService.deleteBookFromCart(cartId, bookId));
    }
}
