package com.carrentalmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * JwtAuthenticationFilter è un filtro di Spring Security.
 * Controlla il token JWT presente nelle richieste in arrivo.
 *
 * Estende OncePerRequestFilter quindi viene eseguito una sola volta per ogni richiesta.
 * Se il token è valido, autentica l'utente nel contesto di sicurezza di Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Service che gestisce creazione e validazione dei token JWT
    private final JwtService jwtService;

    // Service che carica i dati dell'utente dal database
    private final CustomUserDetailsService userDetailsService;

    // Dependency Injection tramite costruttore
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // Controlla la richiesta e autentica l'utente se il token JWT è valido
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Legge l'header Authorization della richiesta
        final String authHeader = request.getHeader("Authorization");

        // Se il token manca o non è Bearer la richiesta prosegue senza autenticazione
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrae il token rimuovendo il prefisso Bearer
        final String token = authHeader.substring(7);

        // Estrae l'email salvata nel token
        final String email = jwtService.extractEmail(token);

        // Se l'email esiste e l'utente non è già autenticato procede con il controllo
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carica l'utente dal database
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Verifica che il token sia valido per quell'utente
            if (jwtService.isTokenValid(token, userDetails.getUsername())) {

                // Crea l'oggetto di autenticazione con utente e ruoli
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Aggiunge i dettagli della richiesta
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Registra l'utente autenticato nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Passa la richiesta al filtro successivo
        filterChain.doFilter(request, response);
    }
}