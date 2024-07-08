package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamchallenge.bookshop.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query(value = "SELECT DISTINCT b.*, bi.* FROM books b " +
                   "LEFT JOIN book_images bi ON b.id = bi.book_id " +
                   "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :input, '%')) " +
                   "ORDER BY b.title " +
                   "LIMIT 5",
            nativeQuery = true)
    List<Book> findByCombinedSimilarity(@Param("input") String input);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.images WHERE b.isThisSlider = true")
    List<Book> findSliderBooks();

    @Query("SELECT b FROM Book b ORDER BY RANDOM() LIMIT :count")
    List<Book> getRandom(@Param("count") Integer count);
}