package com.carrentalmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/*
 * JwtService è un service di Spring che gestisce i token JWT.
 * Si occupa di creare i token, leggere i dati contenuti al loro interno
 * e verificare che siano validi e non scaduti.
 *
 * JWT significa JSON Web Token: un token firmato usato per identificare
 * l'utente nelle richieste protette.
 */
@Service
public class JwtService {

    // Chiave segreta usata per firmare e verificare i token
    @Value("${app.jwt.secret}")
    private String secret;

    // Durata del token in millisecondi
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    // Trasforma la chiave segreta in una SecretKey utilizzabile da JWT
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Crea un token JWT usando l'email dell'utente come subject
    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder().subject(email).issuedAt(now).expiration(expiry).signWith(getKey()).compact();
    }

    // Estrae l'email contenuta nel token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*
     * Metodo di supporto per estrarre un dato specifico dal token.
     * Prima verifica la firma del token usando la chiave segreta.
     */
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();

        return resolver.apply(claims);
    }

    // Verifica che il token appartenga all'email indicata e non sia scaduto
    public boolean isTokenValid(String token, String email) {
        final String tokenEmail = extractEmail(token);

        return tokenEmail.equals(email) && !isExpired(token);
    }

    // Controlla se il token è scaduto
    private boolean isExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);

        return expiration.before(new Date());
    }
}