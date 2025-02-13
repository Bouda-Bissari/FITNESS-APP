package com.feetness.feetness.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            logger.debug("Processing request for URI: {}", requestURI);

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.debug("No valid Authorization header found for URI: {}", requestURI);
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            if (jwt.isEmpty()) {
                logger.warn("Empty JWT token received");
                filterChain.doFilter(request, response);
                return;
            }

            try {
                final String userPhone = jwtService.extractEmail(jwt);
                if (userPhone == null) {
                    logger.warn("Could not extract phone number from JWT");
                    filterChain.doFilter(request, response);
                    return;
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails;
                    try {
                        userDetails = userDetailsService.loadUserByUsername(userPhone);
                    } catch (Exception e) {
                        logger.error("Failed to load user details for phone: {}", userPhone, e);
                        filterChain.doFilter(request, response);
                        return;
                    }

                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("Successfully authenticated user: {}", userPhone);
                    } else {
                        logger.warn("Invalid JWT token for user: {}", userPhone);
                    }
                }
            } catch (Exception e) {
                logger.error("JWT processing error", e);
                SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error in JWT filter", e);
            SecurityContextHolder.clearContext();
            throw e;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Ajoutez ici vos chemins publics qui ne nécessitent pas d'authentification
        return path.startsWith("/api/auth/") || 
               path.equals("/api/public") || 
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs");
    }
}