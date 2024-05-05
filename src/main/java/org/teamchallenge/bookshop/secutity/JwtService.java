package org.teamchallenge.bookshop.secutity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
//    @Value("${secret.key}")
    private static String SECRET_KEY = "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D";
    private static final long EXPIRATION_TIME = 864000000;
    public String extractUsername(String token) {
        return parseJWT(token).getBody().getSubject();
    }

    public static String generateJWT(String subject) {
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(subject) // Set the subject (username)
                .setIssuedAt(new Date()) // Set the issued time
                .setExpiration(expiration) // Set the expiration time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Sign the JWT with the secret key
                .compact(); // Compact the JWT into its final String representation
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
    }

    private boolean isTokenExpired(String jwt) {
        return parseJWT(jwt).getBody().getExpiration().before(new Date());
    }

    private static Jws<Claims> parseJWT(String jwt) {
        // Parse the JWT
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) // Set your secret key here
                .build().parseClaimsJws(jwt);
    }
}
