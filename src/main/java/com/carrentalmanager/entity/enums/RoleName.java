package com.carrentalmanager.entity.enums;

/*
 * RoleName è un enum, cioè un tipo che definisce un insieme fisso di valori.
 * Rappresenta i ruoli disponibili nel sistema.
 *
 * Ogni ruolo avrà permessi diversi configurati nella SecurityConfig.
 */
public enum RoleName {
    CUSTOMER,  // Cliente che effettua le prenotazioni
    EMPLOYEE,  // Operatore che gestisce veicoli e prenotazioni
    ADMIN      // Amministratore con permessi completi
}