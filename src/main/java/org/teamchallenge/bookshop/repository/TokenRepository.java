package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamchallenge.bookshop.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Token findByToken(String token);
}
