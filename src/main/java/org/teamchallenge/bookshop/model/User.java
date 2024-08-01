package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.teamchallenge.bookshop.enums.Role;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;
    @Column(name = "email",unique = true)
    private String email;
    private String phoneNumber;
    private String password;
    private String provider;
    private String providerId;
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    private Role role;

    @ManyToMany
    @JoinTable(name = "users_books",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> favourites = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "users_orders",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders = new ArrayList<>();

}
