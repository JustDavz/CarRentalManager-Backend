package com.carrentalmanager.repository;

import com.carrentalmanager.entity.RentalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * RentalServiceRepository è un repository Spring Data JPA.
 * Gestisce le operazioni sul database per l'entità RentalService.
 *
 * Estende JpaRepository, quindi eredita già i metodi CRUD principali
 * come save, findById, findAll e delete.
 */
public interface RentalServiceRepository extends JpaRepository<RentalService, Long> {

    // Trova solo i servizi attivi
    List<RentalService> findByActiveTrue();
}