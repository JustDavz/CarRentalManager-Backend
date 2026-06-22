package com.carrentalmanager.repository;

import com.carrentalmanager.entity.Vehicle;
import com.carrentalmanager.entity.enums.FuelType;
import com.carrentalmanager.entity.enums.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/*
 * VehicleRepository è un repository Spring Data JPA.
 * Gestisce le operazioni sul database per l'entità Vehicle.
 *
 * Estende JpaRepository, quindi eredita i metodi CRUD principali
 * come save, findById, findAll e delete.
 *
 * Gestisce anche auto, moto e van grazie all'ereditarietà configurata in Vehicle.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Trova tutti i veicoli disponibili
    List<Vehicle> findByAvailableTrue();

    // Trova i veicoli in base alla categoria
    List<Vehicle> findByCategory(VehicleCategory category);

    // Trova i veicoli in base a categoria e carburante
    List<Vehicle> findByCategoryAndFuel(VehicleCategory category, FuelType fuel);

    // Restituisce tutti i veicoli ordinati per prezzo crescente
    List<Vehicle> findAllByOrderByPricePerDayAsc();

    // Cerca veicoli per marca o modello ignorando maiuscole e minuscole
    List<Vehicle> findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(String brand, String model);

    /*
     * Query JPQL (Java Persistence Query Language) personalizzata.
     * Trova i veicoli disponibili con prezzo minore o uguale al prezzo massimo indicato.
     */
    @Query("SELECT v FROM Vehicle v WHERE v.available = true AND v.pricePerDay <= :maxPrice ORDER BY v.pricePerDay ASC")
    List<Vehicle> findAvailableUnderPrice(@Param("maxPrice") BigDecimal maxPrice);
}