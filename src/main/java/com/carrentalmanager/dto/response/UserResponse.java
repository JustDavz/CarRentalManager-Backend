package com.carrentalmanager.dto.response;

import com.carrentalmanager.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
 * UserResponse è un DTO di risposta, cioè rappresenta i dati dell'utente che
 * il backend restituisce al client.
 *
 * Contiene le informazioni principali dell'utente, come email, nome, cognome,
 * telefono, immagine profilo, data di registrazione e ruoli.
 *
 * Non contiene la password, perché decidiamo quali dati esporre nella risposta.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dal backend al client.
 *
 * Il metodo from() converte l'entità User in un oggetto UserResponse
 * più sicuro e più semplice da serializzare in JSON.
 */
public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        String profileImage,
        LocalDateTime registrationDate,
        List<String> roles
) {

    // Costruisce il DTO a partire dall'entità User
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getProfileImage(),
                user.getRegistrationDate(),
                user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList())
        );
    }
}
