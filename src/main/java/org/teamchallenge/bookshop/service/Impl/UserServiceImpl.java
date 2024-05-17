package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.config.BookMapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.exception.UserNotAuthenticatedException;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.service.BookService;
import org.teamchallenge.bookshop.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final BookService bookService;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> getFavouriteBooks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findFavouritesById(userRepository.findIdByEmail(email).get())
                .stream()
                .map(bookMapper::entityToDTO)
                .toList();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.of(userRepository.findById(id)).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateUser(User user) {
        userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void addBookToFavourites(Long id) {
        User user = getAuthenticatedUser();
        List<Book> list = user.getFavourites();
        Book book = bookMapper.dtoToEntity(bookService.getBookById(id));
        if (!list.contains(book)) {
            list.add(book);
        }
        user.setFavourites(list);
        userRepository.save(user);
    }

    @Override
    public void deleteBookFromFavourites(Long id) {
        User user = getAuthenticatedUser();
        List<Book> list = user.getFavourites();
        list.removeIf(x -> x.getId() == id);
        user.setFavourites(list);
        userRepository.save(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return findUserByEmail(email).orElseThrow(UserNotAuthenticatedException::new);
    }
}
