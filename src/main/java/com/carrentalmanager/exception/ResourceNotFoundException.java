package com.carrentalmanager.exception;

/*
 * ResourceNotFoundException è un'eccezione personalizzata usata quando una risorsa richiesta non esiste.
 * Per esempio, quando viene cercato un veicolo con un id non presente nel database.
 *
 * Estende RuntimeException, quindi è un'eccezione non controllata.
 * Viene intercettata dal gestore globale e trasformata in un errore 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {

    // Crea l'eccezione con il messaggio di errore
    public ResourceNotFoundException(String message) {
        super(message);
    }
}