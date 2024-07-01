package org.teamchallenge.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.enums.TokenStatus;
import org.teamchallenge.bookshop.secutity.JwtService;
import org.teamchallenge.bookshop.service.CartService;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(maxAge = 3600, origins = "*")
public class CartController {
    private final CartService cartService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<UUID> createCart() {
        return ResponseEntity.ok(cartService.createCart().getId());
    }

    @Operation(summary = "Get cart by id",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/findById")
    public ResponseEntity<CartDto> getCartById(
            @Parameter(description = "Id of cart")
            @CookieValue(required = false, name = "cartId") UUID cartId,
            HttpServletRequest request) {
        return extractCartId(request, cartId)
                .map(id -> ResponseEntity.ok(cartService.getCartById(id)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
    }

    @Operation(summary = "Add book in cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/add")
    public ResponseEntity<CartDto> addBookToCart(
            @Parameter(description = "Id of cart")
            @CookieValue(required = false, name = "cartId") UUID cartId,
            @RequestParam long bookId,
            HttpServletRequest request) {
        return extractCartId(request, cartId)
                .map(id -> ResponseEntity.ok(cartService.addBookToCart(id, bookId)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
    }

    @Operation(summary = "Update amount of book in cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update")
    public ResponseEntity<CartDto> updateBookQuantityInCart(
            @Parameter(description = "Id of cart")
            @CookieValue(required = false, name = "cartId") UUID cartId,
            @Parameter(description = "Id of book")
            @RequestParam long bookId,
            @Parameter(description = "New quantity of book")
            @RequestParam int quantity,
            HttpServletRequest request) {
        return extractCartId(request, cartId)
                .map(id -> ResponseEntity.ok(cartService.updateQuantity(id, bookId, quantity)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
    }

    @Operation(summary = "Delete book from cart",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete")
    public ResponseEntity<CartDto> deleteBookFromCart(
            @Parameter(description = "Id of cart")
            @CookieValue(required = false, name = "cartId") UUID cartId,
            @Parameter(description = "Id of book")
            @RequestParam long bookId,
            HttpServletRequest request) {
        return extractCartId(request, cartId)
                .map(id -> ResponseEntity.ok(cartService.deleteBookFromCart(id, bookId)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
    }

    private Optional<UUID> extractCartId(HttpServletRequest request, UUID cartId) {
        String jwt = jwtService.extractTokenFromRequest(request);
        if (cartId != null) {
            return Optional.of(cartId);
        } else if (jwt != null && jwtService.checkToken(jwt) == TokenStatus.VALID) {
                return Optional.ofNullable(cartService.getCartIdByUserEmail(jwtService.extractUsername(jwt)));
        } else {
            return Optional.empty();
        }
    }
}