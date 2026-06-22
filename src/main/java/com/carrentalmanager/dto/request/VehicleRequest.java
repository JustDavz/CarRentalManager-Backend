package com.carrentalmanager.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/*
 * VehicleRequest è un DTO di richiesta, cioè rappresenta i dati che il client
 * invia quando vuole creare o modificare un veicolo.
 *
 * Il campo vehicleType indica quale tipo di veicolo creare: CAR, MOTORCYCLE o VAN.
 *
 * I campi specifici, come porte, cilindrata e volume di carico sono facoltativi.
 * Il service userà solo quelli corretti in base al tipo di veicolo indicato.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dalla richiesta HTTP al backend.
 *
 * Le annotazioni di validazione lavorano insieme a @Valid nel controller:
 * se i dati non sono validi, Spring risponde con errore 400.
 */
public record VehicleRequest(

        // Tipo di veicolo obbligatorio
        @NotBlank(message = "Il tipo di veicolo è obbligatorio (CAR, MOTORCYCLE, VAN)")
        String vehicleType,

        // Marca obbligatoria
        @NotBlank(message = "La marca è obbligatoria")
        String brand,

        // Modello obbligatorio
        @NotBlank(message = "Il modello è obbligatorio")
        String model,

        // Anno minimo consentito
        @Min(value = 2000, message = "Anno non valido")
        int year,

        // Categoria obbligatoria
        @NotNull(message = "La categoria è obbligatoria (BASIC, PREMIUM, LUXURY)")
        String category,

        // Tipo di cambio obbligatorio
        @NotNull(message = "Il cambio è obbligatorio (MANUALE, AUTOMATICA)")
        String transmission,

        // Tipo di carburante obbligatorio
        @NotNull(message = "Il carburante è obbligatorio")
        String fuel,

        // Numero di posti minimo
        @Min(value = 1, message = "I posti devono essere almeno 1")
        int seats,

        // Tariffa giornaliera obbligatoria e positiva
        @NotNull(message = "Il prezzo è obbligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "Il prezzo deve essere positivo")
        BigDecimal pricePerDay,

        // Targa obbligatoria
        @NotBlank(message = "La targa è obbligatoria")
        String licensePlate,

        // Campi opzionali comuni
        String image,
        String description,

        // Campi specifici dell'auto
        Integer numberOfDoors,
        String bodyType,

        // Campi specifici della moto
        Integer engineCc,
        String motorcycleType,

        // Campi specifici del van
        BigDecimal cargoVolumeM3,
        Integer maxLoadKg
) {}
