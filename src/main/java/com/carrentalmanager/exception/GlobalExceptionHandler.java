package com.carrentalmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/*
 * GlobalExceptionHandler è il gestore centrale degli errori dell'applicazione.
 * Intercetta le eccezioni lanciate dai controller e restituisce una risposta JSON standard.
 *
 * In questo modo la gestione degli errori rimane in un unico punto
 * e ogni errore viene trasformato in un ApiError con il giusto codice HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gestisce risorse non trovate e restituisce errore 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        ApiError error = new ApiError(404, ex.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Gestisce richieste non valide e restituisce errore 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        ApiError error = new ApiError(400, ex.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /*
     * Gestisce gli errori di validazione dei dati in ingresso.
     * Agisce quando un campo non rispetta annotazioni come @NotBlank, @Email o @Pattern.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().isEmpty() ? "Dati non validi" : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ApiError error = new ApiError(400, msg, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Gestisce qualsiasi errore non previsto e restituisce errore 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        ApiError error = new ApiError(500, "Errore interno del server", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}