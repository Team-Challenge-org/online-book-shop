package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.teamchallenge.bookshop.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByTitleIgnoreCase (String title);

    @Query("SELECT b FROM Book b ORDER BY RANDOM() LIMIT :count")
    List<Book> getRandom(@Param("count") Integer count);
}