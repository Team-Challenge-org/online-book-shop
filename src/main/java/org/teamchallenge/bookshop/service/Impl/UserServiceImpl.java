package org.teamchallenge.bookshop.service.Impl;

import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Optional<User> getUserById(Long id) {
        // TODO
        return Optional.empty();
    }

    @Override
    public User updateUser(User user) {
        // TODO
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        // TODO
         }

    @Override
    public List<User> getAllUsers() {
            // TODO
        return null;
    }


    @Override
    public Optional<User> findUserByEmail(String email) {
        // TODO
        return Optional.empty();
    }
}
