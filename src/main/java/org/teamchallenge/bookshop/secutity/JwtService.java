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
import org.teamchallenge.bookshop.enums.TokenValidationResult;
import org.teamchallenge.bookshop.exception.SecretKeyNotFoundException;
import org.teamchallenge.bookshop.model.PasswordResetToken;
import org.teamchallenge.bookshop.model.Token;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.PasswordTokenRepository;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final PasswordTokenRepository passwordTokenRepository;

    private static final String SECRET_KEY = Optional.ofNullable(System.getenv("SECRET_KEY"))
            .orElseThrow(SecretKeyNotFoundException::new);


    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    public String extractUsername(String token) {

            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("email", String.class);
    }

    public String generateJWT(User user) {
        return Jwts.builder()
                .claim("email", user.getEmail())
                .claim("cartId", user.getCart().getId())
                .subject(user.getEmail())
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
    public String validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passTokenOpt = passwordTokenRepository.findByToken(token);
        if (passTokenOpt.isEmpty()) {
            return TokenValidationResult.INVALID.name();
        }
        PasswordResetToken passToken = passTokenOpt.get();
        return isTokenExpired(passToken) ? TokenValidationResult.EXPIRED.name() : TokenValidationResult.VALID.name();
    }
    private boolean isTokenExpired(PasswordResetToken passToken) {
        return new Date().after(passToken.getExpiryDate());
    }


}