package com.carrentalmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/*
 * ResetPasswordRequest è un DTO di richiesta, cioè rappresenta i dati che
 * l'utente invia quando vuole impostare una nuova password dopo il reset.
 *
 * Contiene il token ricevuto via email e la nuova password scelta dall'utente.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dalla richiesta HTTP al backend.
 *
 * Le annotazioni @NotBlank e @Pattern attivano la validazione automatica insieme
 * a @Valid nel controller: se i dati non sono validi, Spring risponde con errore 400.
 */
public record ResetPasswordRequest(

        /*
         * Token temporaneo ricevuto via email (tramite il servizio MailGun)
         * Il backend verifica validità e scadenza prima di cambiare la password.
         */
        @NotBlank(message = "Il token è obbligatorio")
        String token,

        /*
         * Nuova password obbligatoria.
         * Deve avere almeno 8 caratteri, una minuscola, una maiuscola e un numero.
         * Verrà cifrata con BCrypt prima del salvataggio.
         */
        @NotBlank(message = "La nuova password è obbligatoria")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "La password deve avere almeno 8 caratteri, una maiuscola, una minuscola e un numero")
        String newPassword
) {}
