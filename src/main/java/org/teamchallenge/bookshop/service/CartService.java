package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CartService {

    Cart createCart(Cart cart);

    Optional<Cart> getCartById(Long id);

    Cart updateCart(Cart cart);

    void deleteCart(Long id);

    Cart addBookToCart(Long cartId, Book book, int amount);

    Cart removeBookFromCart(Long cartId, Book book, int amount);

    BigDecimal calculateTotal(Long cartId);

    List<Cart> getAllCarts();
}
