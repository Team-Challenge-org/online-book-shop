package org.teamchallenge.bookshop.secutity;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.teamchallenge.bookshop.enums.TokenStatus;
import org.teamchallenge.bookshop.exception.*;
import org.teamchallenge.bookshop.model.User;
import org.teamchallenge.bookshop.model.request.AuthRequest;
import org.teamchallenge.bookshop.service.UserService;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private final StringRedisTemplate redisTemplate;
    private final UserService userService;

    private static final String SECRET_KEY = Optional.ofNullable(System.getenv("SECRET_KEY"))
            .orElseThrow(SecretKeyNotFoundException::new);

    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 4;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 *60 * 24 * 14;

    public String extractUsername(String token) throws TokenBlacklistedException, TokenExpiredException {
        TokenStatus tokenStatus = checkToken(token);
        if (tokenStatus == TokenStatus.VALID) {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("email", String.class);
        } else if (tokenStatus == TokenStatus.BLACKLISTED){
            throw new TokenBlacklistedException("Token is blacklisted!");
        } else {
            throw new TokenExpiredException("Token is blacklisted!");
        }
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .claim("email", user.getEmail())
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .compact();
    }

    public TokenStatus checkToken(String token) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
            return redisTemplate.hasKey(token) ? TokenStatus.BLACKLISTED : TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenInvalidException("Token is invalid");
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

    public void blacklistToken(String token) {
        Claims payload = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        long time = payload.getExpiration().getTime() - payload.getIssuedAt().getTime();
        if (redisTemplate.hasKey(token)) {
            throw new TokenAlreadyBlacklistedException();
        }
        redisTemplate.opsForValue().set(token, "true", time, TimeUnit.MILLISECONDS);
    }

    @Deprecated
    //testing purpose only
    public ResponseEntity<String> generateExpiredAccessToken(AuthRequest authRequest) {
        User user = userService.findUserByEmail(authRequest.getEmail()).orElseThrow(NotFoundException::new);
        String expiredJwt = Jwts.builder()
                .claim("email", user.getEmail())
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
        return ResponseEntity.ok(expiredJwt);
    }

    @Deprecated
    public ResponseEntity<String> generateExpiredRefreshToken() {
        String expiredJwt = Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
        return ResponseEntity.ok(expiredJwt);
    }
}