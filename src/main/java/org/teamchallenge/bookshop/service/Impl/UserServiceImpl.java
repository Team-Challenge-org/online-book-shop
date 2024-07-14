package org.teamchallenge.bookshop.service.Impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamchallenge.bookshop.config.BookMapper;
import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.exception.UserNotAuthenticatedException;
import org.teamchallenge.bookshop.exception.UserNotFoundException;
import org.teamchallenge.bookshop.model.Book;
import org.teamchallenge.bookshop.model.PasswordResetToken;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.PasswordTokenRepository;
import org.teamchallenge.bookshop.repository.UserRepository;
import org.teamchallenge.bookshop.service.BookService;
import org.teamchallenge.bookshop.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final BookService bookService;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<BookDto> getFavouriteBooks() {
      User user = getAuthenticatedUser();
        return userRepository.findFavouritesById(userRepository.findIdByEmail(user.getEmail()).get())
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


    @Transactional
    public void createPasswordResetTokenForUser(String userEmail, String token) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        PasswordResetToken existingToken = passwordTokenRepository.findByUser(user);
        if (existingToken != null) {
            updateToken(existingToken, token);
        } else {
            PasswordResetToken myToken = new PasswordResetToken();
            myToken.setToken(token);
            myToken.setUser(user);
            myToken.setExpiryDate(calculateExpiryDate());
            passwordTokenRepository.save(myToken);
        }
    }

    private void updateToken(PasswordResetToken existingToken, String token) {
        existingToken.setToken(token);
        existingToken.setExpiryDate(calculateExpiryDate());
        passwordTokenRepository.save(existingToken);
    }

    private Date calculateExpiryDate() {
        return new Date(System.currentTimeMillis() + PasswordResetToken.EXPIRATION_TIME_IN_MILLIS);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return passwordTokenRepository.findByToken(token)
                .filter(passToken -> !isTokenExpired(passToken))
                .map(PasswordResetToken::getUser);
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        return new Date().after(passToken.getExpiryDate());
    }



    @Override
    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return findUserByEmail(email).orElseThrow(UserNotAuthenticatedException::new);
    }

}
