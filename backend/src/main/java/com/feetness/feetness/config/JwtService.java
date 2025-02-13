package com.feetness.feetness.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final String SECRET_KEY = "6B7D3F3B6A5C9C4E2F8A0E4B8D7F0EAB5E6C9D7E6F0A3B7C9E5A2C8F3A8E6D7F";
    private static final long JWT_EXPIRATION = 1000 * 60 * 60 * 10; // 10 heures

    public String extractEmail(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            logger.error("Token expiré lors de l'extraction de l'email: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de l'extraction de l'email: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            if (token == null || token.isEmpty()) {
                logger.error("Token null ou vide");
                return false;
            }

            final String email = extractEmail(token);
            if (email == null || !email.equals(userDetails.getUsername())) {
                logger.warn("Email du token ne correspond pas à l'utilisateur: token={}, user={}", 
                    email, userDetails.getUsername());
                return false;
            }

            if (isTokenExpired(token)) {
                logger.warn("Token expiré pour l'utilisateur: {}", email);
                return false;
            }

            logger.info("Token validé avec succès pour l'utilisateur: {}", email);
            return true;

        } catch (ExpiredJwtException e) {
            logger.error("Token expiré: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            logger.error("Token mal formé: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            logger.error("Signature du token invalide: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("Token non supporté: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la validation du token: {}", e.getMessage());
            return false;
        }
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            Date now = new Date(System.currentTimeMillis());
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

            String token = Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();

            logger.info("Token généré avec succès pour l'utilisateur: {}", userDetails.getUsername());
            return token;
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du token pour {}: {}", 
                userDetails.getUsername(), e.getMessage());
            throw new RuntimeException("Erreur lors de la génération du token", e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Erreur lors de l'extraction des claims: {}", e.getMessage());
            throw e;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            boolean isExpired = expiration.before(new Date());
            if (isExpired) {
                logger.warn("Token expiré. Date d'expiration: {}, Date actuelle: {}", 
                    expiration, new Date());
            }
            return isExpired;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'expiration: {}", e.getMessage());
            return true;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Erreur lors de la génération de la clé de signature: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la génération de la clé de signature", e);
        }
    }
}