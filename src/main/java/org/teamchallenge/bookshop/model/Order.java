package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.teamchallenge.bookshop.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    private String stripePaymentIntentId;
    @ElementCollection
    @MapKeyJoinColumn(name = "books.id")
    @Column(name = "count")
    private Map<Book, Integer> books = new HashMap<>();
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime statusChange;
}
