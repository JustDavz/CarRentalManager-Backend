package com.carrentalmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * SecurityConfig è la classe di configurazione della sicurezza.
 * Definisce quali rotte sono pubbliche, quali richiedono autenticazione
 * e quali sono accessibili solo a determinati ruoli.
 *
 * Configura anche BCrypt per cifrare le password, la sessione stateless
 * basata su JWT e il filtro che controlla il token a ogni richiesta.
 */
@Configuration
public class SecurityConfig {

    // Filtro che controlla il token JWT nelle richieste
    private final JwtAuthenticationFilter jwtAuthFilter;

    // Service che carica l'utente dal database per Spring Security
    private final CustomUserDetailsService userDetailsService;

    // Dependency Injection tramite costruttore
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /*
     * Configura la catena dei filtri di sicurezza.
     * Vengono definite le regole di accesso alle API.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disattiva CSRF (Cross-Site Request Forgery) perché l'app usa JWT ed è stateless
                .csrf(AbstractHttpConfigurer::disable)

                // Disattiva le sessioni lato server: ogni richiesta deve autenticarsi tramite token
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Definisce le regole di autorizzazione per le rotte
                .authorizeHttpRequests(auth -> auth

                        // Rotte pubbliche per autenticazione, login e registrazione
                        .requestMatchers("/api/auth/**").permitAll()

                        // Lettura pubblica di veicoli, servizi e sedi
                        .requestMatchers(HttpMethod.GET, "/api/vehicles/**", "/api/services/**", "/api/branches/**").permitAll()

                        // Creazione, modifica e cancellazione dei veicoli riservate a employee e admin
                        .requestMatchers(HttpMethod.POST, "/api/vehicles/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicles/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicles/**").hasAnyRole("EMPLOYEE", "ADMIN")

                        // Approvazione e rifiuto prenotazioni riservati a employee e admin
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/*/approve", "/api/bookings/*/reject").hasAnyRole("EMPLOYEE", "ADMIN")

                        // Visualizzazione di tutte le prenotazioni riservata a employee e admin
                        .requestMatchers(HttpMethod.GET, "/api/bookings", "/api/bookings/paged").hasAnyRole("EMPLOYEE", "ADMIN")

                        // Le altre rotte delle prenotazioni richiedono un utente autenticato
                        .requestMatchers("/api/bookings/**").authenticated()

                        // Rotte admin riservate solo agli amministratori
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Report accessibili a employee e admin
                        .requestMatchers("/api/reports/**").hasAnyRole("EMPLOYEE", "ADMIN")

                        // Tutte le altre richieste richiedono autenticazione
                        .anyRequest().authenticated()
                )

                // Collega il provider di autenticazione personalizzato
                .authenticationProvider(authenticationProvider())

                // Inserisce il filtro JWT prima del filtro standard username/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Crea il PasswordEncoder BCrypt usato per cifrare e verificare le password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Configura il provider di autenticazione.
     * Collega UserDetailsService con BCrypt.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    // Espone l'AuthenticationManager usato nel login per verificare email e password
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}