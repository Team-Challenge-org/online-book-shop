package org.teamchallenge.bookshop.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamchallenge.bookshop.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

}