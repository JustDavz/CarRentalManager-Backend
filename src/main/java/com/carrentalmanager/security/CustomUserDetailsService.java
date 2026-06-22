package com.carrentalmanager.security;

import com.carrentalmanager.entity.User;
import com.carrentalmanager.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/*
 * CustomUserDetailsService è un service di Spring Security.
 * Fa da ponte tra il database dell'applicazione e il sistema di autenticazione di Spring.
 *
 * Quando Spring deve autenticare un utente, cerca l'utente tramite email,
 * recupera password e ruoli dal database e li trasforma in un oggetto UserDetails.
 *
 * Implementa UserDetailsService, cioè l'interfaccia che Spring Security usa
 * per caricare i dati dell'utente durante il login.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repository usato per cercare l'utente nel database
    private final UserRepository userRepository;

    // Dependency Injection tramite costruttore
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Carica un utente tramite email.
     * Se l'utente non esiste lancia un'eccezione gestita da Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Cerca l'utente nel database
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));

        // Converte i ruoli dell'utente in authorities per Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name())).collect(Collectors.toList()))
                .build();
    }
}