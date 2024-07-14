package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamchallenge.bookshop.model.PasswordResetToken;
import org.teamchallenge.bookshop.model.User;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    PasswordResetToken findByUser(User user);
    void deleteByToken(String token);

}
