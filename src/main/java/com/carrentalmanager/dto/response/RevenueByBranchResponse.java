package com.carrentalmanager.dto.response;

import java.math.BigDecimal;

/*
 * RevenueByBranchResponse è un DTO di risposta, cioè rappresenta i dati che
 * il backend restituisce al client per mostrare l'incasso totale per sede.
 *
 * Contiene il nome della sede e il totale degli incassi associati a quella sede.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dal backend al client.
 *
 * La query nel BookingRepository restituisce righe grezze sotto forma di Object[].
 * Il metodo from() converte ogni riga in un oggetto leggibile e serializzabile in JSON.
 */
public record RevenueByBranchResponse(String branchName, BigDecimal totalRevenue) {

    // Costruisce il DTO a partire dalla riga grezza restituita dalla query JPQL
    public static RevenueByBranchResponse from(Object[] row) {
        String name = (String) row[0];
        BigDecimal total = (BigDecimal) row[1];

        return new RevenueByBranchResponse(name, total);
    }
}