package com.carrentalmanager.controller;

import com.carrentalmanager.dto.request.BookingRequest;
import com.carrentalmanager.dto.response.BookingResponse;
import com.carrentalmanager.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
 * Controller REST per la gestione delle prenotazioni.
 * Espone gli endpoint per creare, visualizzare, approvare, rifiutare e chiudere le prenotazioni.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    // Service che contiene la logica delle prenotazioni
    private final BookingService bookingService;

    // Dependency Injection tramite costruttore
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Crea una nuova prenotazione per l'utente autenticato
    @PostMapping
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody BookingRequest request, Authentication authentication) {
        String email = authentication.getName();
        BookingResponse created = bookingService.create(request, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Restituisce le prenotazioni dell'utente autenticato
    @GetMapping("/me")
    public List<BookingResponse> getMyBookings(Authentication authentication) {
        return bookingService.getMyBookings(authentication.getName());
    }

    /*
     * Restituisce tutte le prenotazioni.
     * Endpoint riservato ad admin e operatori.
     */
    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.getAll();
    }

    // Restituisce tutte le prenotazioni paginate
    @GetMapping("/paged")
    public Page<BookingResponse> getAllPaged(Pageable pageable) {
        return bookingService.getAllPaged(pageable);
    }

    // Approva una prenotazione
    @PutMapping("/{id}/approve")
    public BookingResponse approve(@PathVariable Long id) {
        return bookingService.approve(id);
    }

    // Rifiuta una prenotazione con una motivazione
    @PutMapping("/{id}/reject")
    public BookingResponse reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.getOrDefault("reason", "Nessuna motivazione fornita");

        return bookingService.reject(id, reason);
    }

    // Chiude una prenotazione
    @PutMapping("/{id}/close")
    public BookingResponse close(@PathVariable Long id) {
        return bookingService.close(id);
    }
}
