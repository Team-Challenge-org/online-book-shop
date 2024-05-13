package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.service.CartService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    @Override
    public Optional<Cart> getCartById(Long id) {
        return Optional.of(cartRepository.findById(id)).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Cart updateCart(Cart cart) {
        cartRepository.findById(cart.getId()).orElseThrow(UserNotFoundException::new);
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.findById(id).orElseThrow(UserNotFoundException::new);
        cartRepository.deleteById(id);
    }

    public Cart addBookToCart(Long cartId, Long bookId, int amount) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        Map<Book, Integer> items = cart.getItems();
        items.compute(book, (key, value) -> (value == null) ? amount : value + amount);
        cart.setItems(items);
        return cartRepository.save(cart);
    }

   public Cart removeBookFromCart(Long cartId, Long bookId, int amount) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        Map<Book, Integer> items = cart.getItems();
        if (items.containsKey(book)) {
            items.put(book, Math.max(items.get(book) - amount, 0));
        } else {
            throw new BookNotFoundException();
        }
        cart.setItems(items);
        return cartRepository.save(cart);
    }

    @Override
    public BigDecimal calculateTotal(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(UserNotFoundException::new);
        return cart.getItems()
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
