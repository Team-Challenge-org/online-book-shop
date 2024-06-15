package org.teamchallenge.bookshop.secutity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.teamchallenge.bookshop.exception.SecretKeyNotFoundException;
import org.teamchallenge.bookshop.model.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    private static final String SECRET_KEY = Optional.ofNullable(System.getenv("SECRET_KEY"))
            .orElseThrow(SecretKeyNotFoundException::new);

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
    public String extractUsername(String token) {
        return Jwts.parser()
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public static String generateJWT(User user) {
        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("cartId", user.getCart().getId())
                .subject(user.getEmail())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(jwt);
            return extractUsername(jwt).equals(userDetails.getUsername());
        } catch (ExpiredJwtException e) {
            //expired
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private static SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}