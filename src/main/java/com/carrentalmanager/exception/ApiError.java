package com.carrentalmanager.exception;

import java.time.LocalDateTime;

/*
 * ApiError rappresenta la struttura standard delle risposte di errore.
 * In questo modo tutti gli errori dell'applicazione hanno lo stesso formato JSON.
 */
public class ApiError {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    // Costruttore con tutti i campi dell'errore
    public ApiError(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getter
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
