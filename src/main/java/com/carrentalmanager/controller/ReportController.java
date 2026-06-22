package com.carrentalmanager.controller;

import com.carrentalmanager.dto.response.MostRentedVehicleResponse;
import com.carrentalmanager.dto.response.RevenueByBranchResponse;
import com.carrentalmanager.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * Controller REST per la gestione dei report statistici.
 * Espone endpoint riservati ad admin e operatori.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    // Service che contiene la logica dei report
    private final ReportService reportService;

    // Dependency Injection tramite costruttore
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Restituisce l'incasso totale raggruppato per sede di ritiro
    @GetMapping("/revenue-by-branch")
    public List<RevenueByBranchResponse> revenueByBranch() {
        return reportService.revenueByBranch();
    }

    // Restituisce la classifica dei veicoli più noleggiati
    @GetMapping("/most-rented-vehicles")
    public List<MostRentedVehicleResponse> mostRentedVehicles() {
        return reportService.mostRentedVehicles();
    }
}