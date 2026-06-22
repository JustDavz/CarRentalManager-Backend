package com.carrentalmanager.repository;

import com.carrentalmanager.entity.ServicePrice;
import com.carrentalmanager.entity.enums.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * ServicePriceRepository è un repository Spring Data JPA.
 * Gestisce le operazioni sul database per l'entità ServicePrice.
 *
 * Estende JpaRepository, quindi eredita i metodi CRUD principali
 * come save, findById, findAll e delete.
 */
public interface ServicePriceRepository extends JpaRepository<ServicePrice, Long> {

    // Trova il prezzo di un servizio in base all'id del servizio e alla categoria del veicolo
    Optional<ServicePrice> findByServiceIdAndCategory(Long serviceId, VehicleCategory category);
}
