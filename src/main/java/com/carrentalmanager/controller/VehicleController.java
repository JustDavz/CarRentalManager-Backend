package com.carrentalmanager.controller;

import com.carrentalmanager.dto.request.VehicleRequest;
import com.carrentalmanager.dto.response.VehicleResponse;
import com.carrentalmanager.entity.enums.FuelType;
import com.carrentalmanager.entity.enums.VehicleCategory;
import com.carrentalmanager.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/*
 * Controller REST per la gestione del catalogo veicoli.
 * Espone endpoint per ricerca, dettaglio, paginazione e operazioni CRUD.
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    // Service che contiene la logica dei veicoli
    private final VehicleService vehicleService;

    // Dependency Injection tramite costruttore
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /*
     * Restituisce l'elenco dei veicoli.
     * Supporta la ricerca testuale e filtri.
     */
    @GetMapping
    public List<VehicleResponse> getAll(@RequestParam(required = false) VehicleCategory category, @RequestParam(required = false) FuelType fuel, @RequestParam(required = false) BigDecimal maxPrice, @RequestParam(required = false) String q) {

        // Ricerca per marca o modello
        if (q != null && !q.isBlank()) {
            return vehicleService.searchByText(q);
        }

        // Ricerca tramite filtri
        if (category != null || fuel != null || maxPrice != null) {
            return vehicleService.search(category, fuel, maxPrice);
        }

        // Restituisce catalogo completo
        return vehicleService.getAll();
    }

    // Restituisce il dettaglio di un singolo veicolo
    @GetMapping("/{id}")
    public VehicleResponse getById(@PathVariable Long id) {
        return vehicleService.getById(id);
    }

    // Restituisce l'elenco dei veicoli paginati
    @GetMapping("/paged")
    public Page<VehicleResponse> getAllPaged(Pageable pageable) {
        return vehicleService.getAllPaged(pageable);
    }

    // Crea un nuovo veicolo
    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse created = vehicleService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Modifica un veicolo esistente
    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        return vehicleService.update(id, request);
    }

    // Elimina un veicolo esistente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);

        return ResponseEntity.noContent().build();
    }
}