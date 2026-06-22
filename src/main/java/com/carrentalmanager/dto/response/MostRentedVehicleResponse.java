package com.carrentalmanager.dto.response;

/*
 * MostRentedVehicleResponse è un DTO di risposta, cioè rappresenta i dati che
 * il backend restituisce al client per mostrare i veicoli più noleggiati.
 *
 * Contiene la targa del veicolo e il numero totale di noleggi associati.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dal backend al client.
 *
 * La query nel BookingRepository restituisce righe grezze sotto forma di Object[].
 * Il metodo from() converte ogni riga in un oggetto leggibile e serializzabile in JSON.
 */
public record MostRentedVehicleResponse(String licensePlate, Long rentalCount) {

    // Costruisce il DTO a partire dalla riga grezza restituita dalla query JPQL
    public static MostRentedVehicleResponse from(Object[] row) {
        String plate = (String) row[0];
        Long count = (Long) row[1];

        return new MostRentedVehicleResponse(plate, count);
    }
}
