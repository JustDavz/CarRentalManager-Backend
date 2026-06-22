package com.carrentalmanager.repository;

import com.carrentalmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * UserRepository è un repository Spring Data JPA.
 * Gestisce le operazioni sul database per l'entità User.
 *
 * Estende JpaRepository, quindi eredita i metodi CRUD principali
 * come save, findById, findAll e delete.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Trova un utente tramite email, usato soprattutto per il login
    Optional<User> findByEmail(String email);

    // Trova un utente tramite token di reset password
    Optional<User> findByResetToken(String resetToken);

    // Verifica se esiste già un utente con la stessa email
    boolean existsByEmail(String email);
}