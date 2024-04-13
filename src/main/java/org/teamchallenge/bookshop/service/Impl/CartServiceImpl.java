package org.teamchallenge.bookshop.service.Impl;

import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.service.CartService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public Cart createCart(Cart cart) {
        // TODO
        return null;
    }

    @Override
    public Optional<Cart> getCartById(Long id) {
        // TODO
        return Optional.empty();
    }

    @Override
    public Cart updateCart(Cart cart) {
        // TODO
        return null;
    }

    @Override
    public void deleteCart(Long id) {
        // TODO
    }

    @Override
    public Cart addBookToCart(Long cartId, Book book) {
        // TODO
        return null;
    }

    @Override
    public Cart removeBookFromCart(Long cartId, Long bookId) {
        // TODO
        return null;
    }

    @Override
    public BigDecimal calculateTotal(Long cartId) {
        // TODO
        return null;
    }

    @Override
    public List<Cart> getAllCarts() {
        // TODO
        return null;
    }
}
