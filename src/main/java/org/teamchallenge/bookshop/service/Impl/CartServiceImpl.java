package org.teamchallenge.bookshop.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.CartMapper;
import org.teamchallenge.bookshop.dto.CartDto;
import org.teamchallenge.bookshop.exception.NotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.Cart;
import org.teamchallenge.bookshop.repository.BookRepository;
import org.teamchallenge.bookshop.repository.CartRepository;
import org.teamchallenge.bookshop.service.CartService;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
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
    @Transactional
    public CartDto addBookToCart(UUID cartId, long bookId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        cart.addOrUpdateBook(book, 1);
        cartRepository.save(cart);
        return cartMapper.entityToDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateQuantity(UUID id, long bookId, int quantity) {
        Cart cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        cart.addOrUpdateBook(book, quantity);
        cartRepository.save(cart);
        return cartMapper.entityToDto(cart);
    }

    @Override
    @Transactional
    public CartDto deleteBookFromCart(UUID id, long bookId) {
        Cart cart = cartRepository.findById(id).orElseThrow(NotFoundException::new);
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        cart.deleteBook(book);
        cartRepository.save(cart);
        return cartMapper.entityToDto(cart);
    }
}
