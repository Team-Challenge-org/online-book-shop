package org.teamchallenge.bookshop.secutity;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.teamchallenge.bookshop.exception.SecretKeyNotFoundException;
import org.teamchallenge.bookshop.model.Token;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.repository.TokenRepository;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class JwtService {
    private final TokenRepository tokenRepository;
    private final SecretKey signingKey;

    private static final String SECRET_KEY = Optional.ofNullable(System.getenv("SECRET_KEY"))
            .orElseThrow(SecretKeyNotFoundException::new);
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;
    @Autowired
    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }
    @Transactional
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(signingKey)
                .compact();
    }
    @Transactional
    public String generateRefreshToken(User user) {


        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(signingKey)
                .compact();
    }
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);
            if (isTokenRevoked(token)) {
                return false;
            }
            Date expiration = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload().getExpiration();
            return new Date().before(expiration);
        } catch (JwtException e) {
            return false;
        }
    }
    public boolean isTokenRevoked(String token) {
        return tokenRepository.findByTokenValueAndRevokedTrue(token).isPresent();
    }
    @Transactional
    public void revokeToken(String token) {
        try {
            Optional<Token> optionalToken = tokenRepository.findByTokenValue(token);
            if (optionalToken.isPresent()) {
                Token tokenEntity = optionalToken.get();
                tokenEntity.setRevoked(true);
                Token savedToken = tokenRepository.save(tokenEntity);
                log.info("Token after save: {}", savedToken);
                log.info("Token revoked: {}", token);
            } else {
                log.warn("Token not found: {}", token);
            }
        } catch (Exception e) {
            log.error("Error revoking token: {}", token, e);
            throw e;
        }
    }


    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}