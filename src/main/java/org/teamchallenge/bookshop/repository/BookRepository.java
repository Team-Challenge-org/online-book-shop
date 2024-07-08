package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamchallenge.bookshop.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query(value = "SELECT DISTINCT b.*, book_images.*, \n" +
                   "       CASE \n" +
                   "           WHEN b.title ILIKE :input THEN 1.0\n" +
                   "           WHEN b.title ILIKE :input || '%' THEN 0.8\n" +
                   "           WHEN b.title ILIKE '%' || :input || '%' THEN 0.6\n" +
                   "       END AS relevance\n" +
                   "FROM books b \n" +
                   "LEFT JOIN book_images ON book_images.book_id = b.id \n" +
                   "WHERE b.title ILIKE '%' || :input || '%'\n" +
                   "ORDER BY relevance DESC, b.title\n" +
                   "LIMIT 5",
            nativeQuery = true)
    List<Book> findByCombinedSimilarity(@Param("input") String input);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.images WHERE b.isThisSlider = true")
    List<Book> findSliderBooks();

    @Query("SELECT b FROM Book b ORDER BY RANDOM() LIMIT :count")
    List<Book> getRandom(@Param("count") Integer count);
}