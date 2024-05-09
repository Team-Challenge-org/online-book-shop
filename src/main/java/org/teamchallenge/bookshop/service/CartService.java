package org.teamchallenge.bookshop.service;


import org.teamchallenge.bookshop.model.Cart;

import java.math.BigDecimal;
import java.util.Optional;

public interface CartService {

    Optional<Cart> getCartById(Long id);

    Cart updateCart(Cart cart);

    void deleteCart(Long id);


    Cart addBookToCart(Long cartId, Long bookId, int amount);

    Cart removeBookFromCart(Long cartId, Long bookId, int amount);


    BigDecimal calculateTotal(Long cartId);
}
