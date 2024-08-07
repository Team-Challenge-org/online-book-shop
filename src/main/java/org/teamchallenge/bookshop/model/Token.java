package org.teamchallenge.bookshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String tokenValue;

    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean revoked = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Token(String tokenValue, LocalDateTime expiryDate) {
        this.tokenValue = tokenValue;
        this.expiryDate = expiryDate;
    }


}
