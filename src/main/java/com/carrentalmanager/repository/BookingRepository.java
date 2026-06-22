package com.carrentalmanager.repository;

import com.carrentalmanager.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
 * BookingRepository è un repository Spring Data JPA.
 * Gestisce le operazioni sul database per l'entità Booking.
 *
 * Estende JpaRepository, quindi eredita già i metodi CRUD principali
 * come save, findById, findAll e delete.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Trova tutte le prenotazioni associate a un utente
    List<Booking> findByUserId(Long userId);

    /*
     * Calcola l'incasso totale per ogni sede di ritiro.
     * Restituisce righe grezze Object[] con nome sede e totale incassato.
     */
    @Query("SELECT b.pickupBranch.name, SUM(b.totalPrice) FROM Booking b GROUP BY b.pickupBranch.name ORDER BY SUM(b.totalPrice) DESC")
    List<Object[]> totalRevenueByBranch();

    /*
     * Calcola i veicoli più noleggiati.
     * Restituisce righe grezze Object[] con targa e numero di noleggi.
     */
    @Query("SELECT b.vehicle.licensePlate, COUNT(b) FROM Booking b GROUP BY b.vehicle.licensePlate ORDER BY COUNT(b) DESC")
    List<Object[]> mostRentedVehicles();
}
