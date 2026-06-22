package com.carrentalmanager.entity.enums;

/*
 * BookingStatus è un enum, cioè un tipo che definisce un insieme fisso di valori.
 * Rappresenta gli stati possibili di una prenotazione.
 *
 * Permette di gestire il ciclo di vita del noleggio:
 * IN_ATTESA -> APPROVATO -> CHIUSO oppure IN_ATTESA -> RIFIUTATO.
 */
public enum BookingStatus {
    IN_ATTESA,  // Prenotazione appena creata, da approvare
    APPROVATO,  // Prenotazione confermata dall'operatore
    RIFIUTATO,  // Prenotazione respinta dall'operatore
    CHIUSO      // Noleggio concluso
}