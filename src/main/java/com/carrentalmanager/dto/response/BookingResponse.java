package com.carrentalmanager.dto.response;

import com.carrentalmanager.entity.Booking;
import com.carrentalmanager.entity.RentalService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
 * BookingResponse è un DTO di risposta, cioè rappresenta i dati di una
 * prenotazione che il backend restituisce al client.
 *
 * È un record usato per evitare di restituire direttamente l'entità Booking,
 * che contiene oggetti annidati come User, Vehicle e Branch (sedi).
 *
 * In questo modo vengono restituiti solo i dati utili, senza informazioni
 * sensibili come la password dell'utente.
 *
 * La risposta JSON risulta più semplice e più sicura.
 */
public record BookingResponse(
        Long id,
        Long userId,
        String userEmail,
        Long vehicleId,
        String vehicleBrand,
        String vehicleModel,
        Long pickupBranchId,
        String pickupBranchName,
        List<String> services,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalPrice,
        String status,
        String rejectionReason,
        LocalDateTime createdAt
) {

    /*
     * Metodo factory statico.
     * Converte un'entità Booking in un DTO BookingResponse.
     */
    public static BookingResponse from(Booking booking) {
        // Estrae i nomi dei servizi scelti
        List<String> serviceNames = booking.getServices() == null ? List.of() : booking.getServices().stream().map(RentalService::getName).collect(Collectors.toList());

        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getUser().getEmail(),
                booking.getVehicle().getId(),
                booking.getVehicle().getBrand(),
                booking.getVehicle().getModel(),
                booking.getPickupBranch().getId(),
                booking.getPickupBranch().getName(),
                serviceNames,
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getTotalPrice(),
                booking.getStatus().name(),
                booking.getRejectionReason(),
                booking.getCreatedAt()
        );
    }
}
