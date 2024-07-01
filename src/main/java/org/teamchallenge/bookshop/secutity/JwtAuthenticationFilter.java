package org.teamchallenge.bookshop.secutity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.teamchallenge.bookshop.enums.TokenStatus;
import org.teamchallenge.bookshop.exception.ErrorObject;
import org.teamchallenge.bookshop.exception.TokenBlacklistedException;
import org.teamchallenge.bookshop.exception.TokenExpiredException;
import org.teamchallenge.bookshop.exception.TokenInvalidException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.checkToken(jwt) == TokenStatus.VALID && jwtService.extractUsername(jwt).equals(userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (TokenBlacklistedException | TokenInvalidException e) {
            handleException(response, e);
            return;
        } catch (TokenExpiredException e) {
            Optional<String> refreshToken = Optional.empty();
            if (request.getCookies() != null) {
                refreshToken =  Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals("refreshToken"))
                        .map(Cookie::getValue)
                        .findAny();
            }
            if (refreshToken.isPresent()) {
                try {
                    jwtService.checkToken(refreshToken.get());
                } catch (TokenBlacklistedException | TokenInvalidException ex) {
                    handleException(response, ex);
                    return;
                } catch (TokenExpiredException ex) {
                    handleException(response, new TokenExpiredException("Refresh token is expired, so you need to log in again!"));
                }
            } else {
                handleException(response, new TokenExpiredException("Access token is expired, but refresh token is not provided!"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, RuntimeException e) throws IOException {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
        errorObject.setMessage(e.getMessage());
        errorObject.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Convert the ErrorObject to JSON
        ObjectMapper mapper = new ObjectMapper();
        String jsonError = mapper.writeValueAsString(errorObject);

        // Set response status and content type
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Write the JSON error object to the response
        response.getWriter().write(jsonError);
    }
}
