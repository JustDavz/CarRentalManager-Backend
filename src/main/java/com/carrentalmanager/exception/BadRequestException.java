package com.carrentalmanager.exception;

/*
 * BadRequestException è un'eccezione personalizzata usata per richieste non valide.
 * Viene usata quando l'errore dipende dalla logica della richiesta, ad esempio una data di fine noleggio precedente alla data di inizio.
 *
 * Questa eccezione verrà gestita come errore 400 Bad Request.
 */
public class BadRequestException extends RuntimeException {

    // Crea l'eccezione con il messaggio di errore
    public BadRequestException(String message) {
        super(message);
    }
}