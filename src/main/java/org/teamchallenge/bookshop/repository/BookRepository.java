package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamchallenge.bookshop.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query(value = "SELECT DISTINCT b.*, book_images.*, " +
            "similarity(b.title, :input) AS sim_score, " +
            "(LENGTH(b.title) / (1 + levenshtein(b.title, :input))) AS lev_score, " +
            "(similarity(b.title, :input) * 0.5 + (LENGTH(b.title) / (1 + levenshtein(b.title, :input))) * 0.5) AS relevance " +
            "FROM books b " +
            "LEFT JOIN book_images ON book_images.book_id = b.id " +
            "WHERE similarity(b.title, :input) > 0.2 " +
            "OR levenshtein(b.title, :input) < (LENGTH(b.title) / 2) " +
            "ORDER BY relevance DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<Book> findByCombinedSimilarity(@Param("input") String input);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.images WHERE b.isThisSlider = true")
    List<Book> findSliderBooks();

    @Query("SELECT b FROM Book b ORDER BY RANDOM() LIMIT :count")
    List<Book> getRandom(@Param("count") Integer count);
}