package com.carrentalmanager.repository;

import com.carrentalmanager.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * BranchRepository è un repository Spring Data JPA.
 * Gestisce le operazioni sul database per l'entità Branch.
 *
 * Estende JpaRepository, quindi eredita già i metodi CRUD principali
 * come save, findById, findAll e delete.
 */
public interface BranchRepository extends JpaRepository<Branch, Long> {
}