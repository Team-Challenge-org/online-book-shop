package org.teamchallenge.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamchallenge.bookshop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
