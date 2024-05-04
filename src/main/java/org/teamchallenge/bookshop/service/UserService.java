package org.teamchallenge.bookshop.service;

import org.teamchallenge.bookshop.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    Optional<User> findUserByEmail(String email);
}
