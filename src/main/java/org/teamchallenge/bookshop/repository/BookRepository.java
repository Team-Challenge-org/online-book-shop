package org.teamchallenge.bookshop.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.teamchallenge.bookshop.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByTitle (String title);

        @Query("SELECT b FROM Book b" +
                " LEFT JOIN b.authors a WHERE (a.fullName = :authorName OR a IS NULL)" +
                " AND (:category IS NULL OR b.category = :category)" +
                " AND (:priceMax IS NULL OR b.price < :priceMax)" +
                " AND (:priceMin IS NULL OR b.price > :priceMin)" +
                " ORDER BY" +
                " CASE WHEN :sortTimeAdded = 'ASC' THEN b.timeAdded END ASC," +
                " CASE WHEN :sortTimeAdded = 'DESC' THEN b.timeAdded END DESC," +
                " CASE WHEN :sortPrice = 'ASC' THEN b.price END ASC," +
                " CASE WHEN :sortPrice = 'DESC' THEN b.price END DESC")
        List<Book> findSorted(@Param("authorName") String authorName,
                              @Param("category") String category,
                              @Param("sortTimeAdded") String sortTimeAdded,
                              @Param("sortPrice") String sortPrice,
                              @Param("priceMax") Float priceMax,
                              @Param("priceMin") Float priceMin);

}