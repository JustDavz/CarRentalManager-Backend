package com.carrentalmanager.repository;

import com.carrentalmanager.entity.Role;
import com.carrentalmanager.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * RoleRepository è un repository Spring Data JPA.
 * Gestisce le operazioni sul database per l'entità Role.
 *
 * Estende JpaRepository, quindi eredita già i metodi CRUD principali
 * come save, findById, findAll e delete.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Trova un ruolo tramite il suo nome
    Optional<Role> findByName(RoleName name);
}