package org.teamchallenge.bookshop.service;


import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.model.Cart;

import java.math.BigDecimal;
import java.util.Optional;

public interface CartService {

    CartDto getCartById(Long id);

    Cart updateCart(Cart cart);
    CartDto clearCart();
    CartDto addBookToCart( Long bookId, int amount);

    CartDto removeBookFromCart(Long bookId, int amount);

    BigDecimal calculateTotal(Long cartId);


    BigDecimal getTotalInCart();
}
