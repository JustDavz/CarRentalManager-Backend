package com.carrentalmanager.controller;

import com.carrentalmanager.dto.request.ForgotPasswordRequest;
import com.carrentalmanager.dto.request.LoginRequest;
import com.carrentalmanager.dto.request.RegisterRequest;
import com.carrentalmanager.dto.request.ResetPasswordRequest;
import com.carrentalmanager.dto.response.AuthResponse;
import com.carrentalmanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 * Controller REST dedicato all'autenticazione:
 * registrazione, login e recupero password.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Service che contiene la logica di autenticazione
    private final AuthService authService;

    // Dependency Injection tramite costruttore
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
     * Registra un nuovo utente.
     * Riceve i dati dal body della richiesta e restituisce token e dati utente.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * Effettua il login dell'utente.
     * Se le credenziali sono corrette restituisce un token JWT.
     */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /*
     * Avvia la procedura di recupero password.
     * Se l'email è registrata viene inviato un token di reset.
     */
    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.email());

        return Map.of("message", "Se l'email è registrata, riceverai le istruzioni per il reset della password.");
    }

    // Aggiorna la password usando il token ricevuto via email
    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.token(), request.newPassword());

        return Map.of("message", "Password aggiornata con successo!");
    }
}
