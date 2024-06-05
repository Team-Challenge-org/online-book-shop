package org.teamchallenge.bookshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.teamchallenge.bookshop.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByTitleIgnoreCase (String title);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.images WHERE b.isThisSlider = false")
    Page<Book> findAllBooks(Pageable pageable);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.images WHERE b.isThisSlider = true")
    List<Book> findSliderBooks();

    @Query("SELECT b FROM Book b ORDER BY RANDOM() LIMIT :count")
    List<Book> getRandom(@Param("count") Integer count);
}