package com.carrentalmanager.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/*
 * BookingRequest è un DTO di richiesta, cioè rappresenta i dati che il cliente
 * invia quando vuole prenotare un noleggio.
 *
 * Indica quale veicolo prenotare, dove ritirarlo, le date del noleggio e gli
 * eventuali servizi accessori da aggiungere.
 *
 * Il prezzo e lo stato iniziale della prenotazione vengono gestiti dal backend.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dalla richiesta HTTP al backend.
 *
 * Java genera in automatico il costruttore, i metodi di accesso
 * (vehicleId(), ...), equals, hashCode e toString.
 *
 * Le annotazioni @NotNull attivano la validazione automatica insieme a @Valid
 * nel controller: se un campo obbligatorio manca, Spring risponde con errore 400.
 */

public record BookingRequest(

        // Id del veicolo da prenotare: obbligatorio
        @NotNull(message = "L'id del veicolo è obbligatorio")
        Long vehicleId,

        // Id della sede dove ritirare il veicolo: obbligatorio.
        @NotNull(message = "L'id della sede di ritiro è obbligatorio")
        Long pickupBranchId,

        // Data di inizio noleggio: obbligatoria
        @NotNull(message = "La data di inizio è obbligatoria")
        LocalDate startDate,

        // Data di fine noleggio: obbligatoria (inoltre il backend controlla che sia dopo la data di inizio)
        @NotNull(message = "La data di fine è obbligatoria")
        LocalDate endDate,

        // Id dei servizi accessori da aggiungere che è FACOLTATIVO (può essere null o vuoto).
        List<Long> serviceIds
) {}
