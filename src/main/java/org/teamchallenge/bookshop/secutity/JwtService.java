package org.teamchallenge.bookshop.secutity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.teamchallenge.bookshop.exception.SecretKeyNotFoundException;
import org.teamchallenge.bookshop.model.Token;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.TokenRepository;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {
        private final TokenRepository tokenRepository;

    private static final String SECRET_KEY = Optional.ofNullable(System.getenv("SECRET_KEY"))
            .orElseThrow(SecretKeyNotFoundException::new);


    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        if (claims.containsKey("email")) {
            return claims.get("email", String.class);
        }
        else if (claims.containsKey("phoneNumber")) {
            return claims.get("phoneNumber", String.class);
        }
        else {
            return claims.getSubject();
        }
    }

    public String generateJWT(User user) {
        Map<String, Object> claims = new HashMap<>();
        if (user.getEmail() != null) {
            claims.put("email", user.getEmail());
        }
        if (user.getPhoneNumber() != null) {
            claims.put("phoneNumber", user.getPhoneNumber());
        }
        claims.put("cartId", user.getCart().getId());

        String subject = user.getEmail() != null ? user.getEmail() : user.getPhoneNumber();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
            return true;
        } catch ( JwtException e) {
            return false;
        }
    }
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Token blacklistToken(String jwt) {
        Claims payload = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(jwt).getPayload();
        LocalDateTime localDateTime = payload.getExpiration().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return new Token(jwt, localDateTime);
    }
    public boolean isTokenBlacklisted(String jwt) {
        Token token = tokenRepository.findByToken(jwt);
        return token != null;
    }
}