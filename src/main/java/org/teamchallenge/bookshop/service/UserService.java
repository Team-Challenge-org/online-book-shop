package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.dto.BookDto;
import org.teamchallenge.bookshop.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<BookDto> getFavouriteBooks();

    Optional<User> getUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    void changeUserPassword(User user, String password);

    User getAuthenticatedUser();

    Optional<User> findUserByEmail(String email);

    void addBookToFavourites(Long id);

    void deleteBookFromFavourites(Long id);

    void createPasswordResetTokenForUser(String userEmail, String token);

    Optional<User> getUserByPasswordResetToken(String token);
}
