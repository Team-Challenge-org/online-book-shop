package org.teamchallenge.bookshop.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.mapper.CartMapper;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.service.CartService;
import org.teamchallenge.bookshop.service.UserService;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final CartMapper cartMapper;

    @Override
    public Cart createCart() {
        Cart cart = new Cart();
        cart.setIsPermanent(false);
        cart.setLastModified(LocalDate.now());
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public CartDto getCartById(UUID id) {
        Cart cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        return cartMapper.entityToDto(cart);
    }

    @Override
    public UUID getCartIdByUserEmail(String email) {
        User user = userService.findUserByEmail(email).orElseThrow(NotFoundException::new);
        return user.getCart().getId();
    }

    @Override
    @Transactional
    public CartDto addBookToCart(UUID cartId, long bookId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        addOrUpdateBook(cart, book, 1);
        cartRepository.save(cart);
        return cartMapper.entityToDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateQuantity(UUID id, long bookId, int quantity) {
        Cart cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        addOrUpdateBook(cart, book, quantity);
        cartRepository.save(cart);
        return cartMapper.entityToDto(cart);
    }

    @Override
    @Transactional
    public CartDto deleteBookFromCart(UUID id, long bookId) {
        Cart cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        deleteBook(cart, book);
        cartRepository.save(cart);
        return cartMapper.entityToDto(cart);
    }

    private void addOrUpdateBook(Cart cart, Book book, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        cart.getItems().merge(book, quantity, Integer::sum);
        cart.setLastModified(LocalDate.now());
    }

    private void deleteBook(Cart cart, Book book) {
        cart.getItems().remove(book);
        cart.setLastModified(LocalDate.now());
    }
}
