package com.carrentalmanager.dto.request;

import jakarta.validation.constraints.NotBlank;

/*
 * LoginRequest è un DTO di richiesta, cioè rappresenta i dati che l'utente
 * invia quando vuole eseguire il login.
 *
 * Contiene email e password. Questi dati vengono inviati dalla richiesta HTTP
 * al backend per verificare le credenziali dell'utente.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dalla richiesta HTTP al backend.
 *
 * Si usa @NotBlank perché email e password sono campi obbligatori.
 * In fase di login non serve validare il formato dell'email perché conta
 * solo verificare se le credenziali coincidono con un utente esistente.
 */
public record LoginRequest(

        // Email inserita dall'utente per accedere
        @NotBlank(message = "L'email è obbligatoria")
        String email,

        // Password inserita dall'utente per accedere
        @NotBlank(message = "La password è obbligatoria")
        String password
) {}
