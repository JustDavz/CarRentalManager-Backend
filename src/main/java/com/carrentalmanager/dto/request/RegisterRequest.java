package com.carrentalmanager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/*
 * RegisterRequest è un DTO di richiesta, cioè rappresenta i dati che il client
 * invia quando vuole registrarsi.
 *
 * Contiene email, password, nome, cognome e telefono.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dalla richiesta HTTP al backend.
 *
 * Java genera in automatico il costruttore, i metodi di accesso,
 * equals, hashCode e toString.
 *
 * Le annotazioni @NotBlank, @Email e @Pattern attivano la validazione automatica
 * insieme a @Valid nel controller: se i dati non sono validi, Spring risponde con errore 400.
 */
public record RegisterRequest(

        // Email obbligatoria e in formato valido
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Email inserita non valida")
        String email,

        /*
         * Password obbligatoria di almeno 8 caratteri.
         * Deve contenere almeno una minuscola, una maiuscola e un numero.
         * La password viene poi cifrata con BCrypt prima di essere salvata.
         */
        @NotBlank(message = "La password è obbligatoria")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "La password deve avere almeno 8 caratteri, una maiuscola, una minuscola e un numero")
        String password,

        // Nome obbligatorio
        @NotBlank(message = "Il nome è obbligatorio")
        String firstName,

        // Cognome obbligatorio
        @NotBlank(message = "Il cognome è obbligatorio")
        String lastName,

        // Telefono facoltativo
        String phone
) {}