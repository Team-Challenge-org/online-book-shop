package org.teamchallenge.bookshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.CartMapper;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.exception.BookNotFoundException;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.service.CartService;
import org.teamchallenge.bookshop.service.UserService;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final CartMapper cartMapper;
    @Override
    public CartDto getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return cartMapper.entityToDTO(cart);
    }

    @Override
    public Cart updateCart(Cart cart) {
        cartRepository.findById(cart.getId()).orElseThrow(UserNotFoundException::new);
        return cartRepository.save(cart);
    }

    @Override
    public CartDto clearCart() {
        User user = userService.getAuthenticatedUser();
        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow(NotFoundException::new);
        cart.getItems().clear();
        cart.setTotal(BigDecimal.ZERO);
        user.setCart(cart);
        cartRepository.save(cart);
        return cartMapper.entityToDTO(cart);
    }
    @Override
    public CartDto addBookToCart(Long bookId, int amount) {
        User user = userService.getAuthenticatedUser();
        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow(UserNotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        Map<Book, Integer> items = cart.getItems();
        items.compute(book, (key, value) -> (value == null) ? amount : value + amount);
        cart.setItems(items);
        BigDecimal total = calculateTotal(cart.getId());
        cart.setTotal(total);
        user.setCart(cart);
        cartRepository.save(cart);
        return  cartMapper.entityToDTO(cart);
    }
    @Override
   public CartDto removeBookFromCart( Long bookId, int amount) {
        User user =userService.getAuthenticatedUser();
        Cart cart = cartRepository.findById(user.getCart().getId()).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        Map<Book, Integer> items = cart.getItems();
        if (items.containsKey(book)) {
            items.put(book, Math.max(items.get(book) - amount, 0));
        } else {
            throw new BookNotFoundException();
        }
        cart.setItems(items);
        BigDecimal total = calculateTotal(cart.getId());
        cart.setTotal(total);
        user.setCart(cart);
        cartRepository.save(cart);
        return cartMapper.entityToDTO(cart);
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

    @Override
    public BigDecimal getTotalInCart() {
        User user = userService.getAuthenticatedUser();
        return calculateTotal(user.getCart().getId());
    }
}
