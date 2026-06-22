package com.carrentalmanager.service;

import com.carrentalmanager.dto.response.MostRentedVehicleResponse;
import com.carrentalmanager.dto.response.RevenueByBranchResponse;
import com.carrentalmanager.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
 * ReportService è un service di Spring che contiene la logica dei report statistici.
 * Recupera i dati aggregati dal BookingRepository e li converte in DTO di risposta.
 *
 * Viene usato per mostrare statistiche come l'incasso totale per sede
 * e la classifica dei veicoli più noleggiati.
 */
@Service
public class ReportService {

    // Repository usato per recuperare i dati aggregati delle prenotazioni
    private final BookingRepository bookingRepository;

    // Dependency Injection tramite costruttore
    public ReportService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Restituisce l'incasso totale per ogni sede di ritiro
    public List<RevenueByBranchResponse> revenueByBranch() {
        return bookingRepository.totalRevenueByBranch().stream().map(RevenueByBranchResponse::from).collect(Collectors.toList());
    }

    // Restituisce la classifica dei veicoli più noleggiati
    public List<MostRentedVehicleResponse> mostRentedVehicles() {
        return bookingRepository.mostRentedVehicles().stream().map(MostRentedVehicleResponse::from).collect(Collectors.toList());
    }
}