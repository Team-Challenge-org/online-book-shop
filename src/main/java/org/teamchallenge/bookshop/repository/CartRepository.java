package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamchallenge.bookshop.model.Cart;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

}