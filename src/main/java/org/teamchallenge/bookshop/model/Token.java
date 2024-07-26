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

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean revoked;

    public Token(String tokenValue, LocalDateTime expiryDate) {
        this.tokenValue = tokenValue;
        this.expiryDate = expiryDate;
        this.revoked = false;
    }


}
