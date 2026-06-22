package com.carrentalmanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/*
 * ForgotPasswordRequest è un DTO di richiesta, cioè rappresenta i dati che
 * l'utente invia quando ha dimenticato la password e chiede di reimpostarla.
 *
 * Contiene solo l'email. Il backend genera un token temporaneo e lo invia
 * a quell'indirizzo tramite il servizio Mailgun.
 *
 * È un record: una classe immutabile e compatta usata come DTO, cioè un
 * oggetto che serve solo a trasferire i dati dalla richiesta HTTP al backend.
 *
 * Le annotazioni di validazione lavorano insieme a @Valid nel controller: se
 * l'email non è presente o non è valida, Spring risponde con errore 400.
 */

public record ForgotPasswordRequest(

        // L'email deve essere presente (@NotBlank) e in un formato valido (@Email).
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Email inserita non valida")
        String email
) {}