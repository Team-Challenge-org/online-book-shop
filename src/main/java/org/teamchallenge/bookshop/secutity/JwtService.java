package org.teamchallenge.bookshop.secutity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    //    @Value("${secret.key}")
    private static String SECRET_KEY = "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D";
    private static final long EXPIRATION_TIME = 1000* 60 * 60 * 24;
    public String extractUsername(String token) {
        return parseJWT(token).getBody().getSubject();
    }

    public static String generateJWT(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
    }

    private boolean isTokenExpired(String jwt) {
        return parseJWT(jwt).getBody().getExpiration().before(new Date());
    }

    private static Jws<Claims> parseJWT(String jwt) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt);
    }
    private static SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}