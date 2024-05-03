package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.service.CartService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> getCartById(Long id) {
        return Optional.of(cartRepository.findById(id)).orElseThrow(NotFoundException::new);
    }

    @Override
    public Cart updateCart(Cart cart) {
        cartRepository.findById(cart.getId()).orElseThrow(NotFoundException::new);
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.findById(id).orElseThrow(NotFoundException::new);
        cartRepository.deleteById(id);
    }

    @Override
    public Cart addBookToCart(Long cartId, Book book, int amount) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        Map<Book, Integer> items = cart.getItems();
        items.compute(book, (key, value) -> (value == null) ? amount : value + amount);
        cart.setItems(items);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeBookFromCart(Long cartId, Book book, int amount) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        Map<Book, Integer> items = cart.getItems();
        if (items.containsKey(book)) {
            items.put(book, Math.max(items.get(book) - amount, 0));
        } else {
            throw new NotFoundException();
        }
        cart.setItems(items);
        return cartRepository.save(cart);
    }

    @Override
    public BigDecimal calculateTotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        return cart.getItems()
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
}
