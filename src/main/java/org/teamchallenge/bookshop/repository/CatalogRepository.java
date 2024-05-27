package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamchallenge.bookshop.model.Catalog;

public interface CatalogRepository  extends JpaRepository<Catalog,Long> {
}
