package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamchallenge.bookshop.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
