package com.carrentalmanager.dto.response;

/*
 * AuthResponse è un DTO di risposta, cioè rappresenta i dati che il backend
 * restituisce al client dopo un login o una registrazione andati a buon fine.
 *
 * Contiene il token JWT, il tipo di token e i dati dell'utente.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dal backend al client.
 */
public record AuthResponse(
        String token,
        String type,
        UserResponse user
) {

    /*
     * Costruttore semplificato.
     * Permette di creare la risposta passando solo token e utente.
     */
    public AuthResponse(String token, UserResponse user) {
        this(token, "Bearer", user); // Il tipo di token è sempre Bearer
    }
}