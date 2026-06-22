package com.carrentalmanager.service;

import com.carrentalmanager.dto.request.VehicleRequest;
import com.carrentalmanager.dto.response.VehicleResponse;
import com.carrentalmanager.entity.*;
import com.carrentalmanager.entity.enums.*;
import com.carrentalmanager.exception.BadRequestException;
import com.carrentalmanager.exception.ResourceNotFoundException;
import com.carrentalmanager.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/*
 * VehicleService è un service di Spring che contiene la logica dei veicoli.
 * Gestisce creazione, lettura, modifica, eliminazione, ricerca e paginazione.
 *
 * Gestisce anche i tre tipi concreti di veicolo: Car, Motorcycle e Van.
 */
@Service
public class VehicleService {

    // Repository usato per gestire i veicoli nel database
    private final VehicleRepository vehicleRepository;

    // Dependency Injection tramite costruttore
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Restituisce tutti i veicoli convertiti in DTO di risposta
    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll().stream().map(VehicleResponse::from).collect(Collectors.toList());
    }

    // Restituisce un veicolo tramite id oppure lancia errore 404
    public VehicleResponse getById(Long id) {
        Vehicle v = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Veicolo non trovato con id " + id));

        return VehicleResponse.from(v);
    }

    /*
     * Cerca i veicoli usando filtri opzionali.
     * Può filtrare per categoria, carburante o prezzo massimo.
     */
    public List<VehicleResponse> search(VehicleCategory category, FuelType fuel, BigDecimal maxPrice) {
        List<Vehicle> result;

        if (category != null && fuel != null) {
            result = vehicleRepository.findByCategoryAndFuel(category, fuel);
        } else if (category != null) {
            result = vehicleRepository.findByCategory(category);
        } else if (maxPrice != null) {
            result = vehicleRepository.findAvailableUnderPrice(maxPrice);
        } else {
            result = vehicleRepository.findAll();
        }

        return result.stream().map(VehicleResponse::from).collect(Collectors.toList());
    }

    /*
     * Crea un nuovo veicolo.
     * Usa una logica factory per costruire il tipo corretto: Car, Motorcycle o Van.
     */
    public VehicleResponse create(VehicleRequest req) {

        // Crea l'oggetto del tipo corretto
        Vehicle vehicle = buildFromType(req);

        // Imposta i campi comuni a tutti i veicoli
        fillCommonFields(vehicle, req);

        // Salva il veicolo e restituisce il DTO di risposta
        Vehicle saved = vehicleRepository.save(vehicle);

        return VehicleResponse.from(saved);
    }

    /*
     * Modifica un veicolo esistente.
     * Aggiorna i campi comuni e i campi specifici del tipo concreto.
     */
    public VehicleResponse update(Long id, VehicleRequest req) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Veicolo non trovato con id " + id));

        // Aggiorna i campi comuni del veicolo
        fillCommonFields(vehicle, req);

        // Aggiorna i campi specifici se il veicolo è un'auto
        if (vehicle instanceof Car car) {
            if (req.numberOfDoors() != null) car.setNumberOfDoors(req.numberOfDoors());
            if (req.bodyType() != null) car.setBodyType(BodyType.valueOf(req.bodyType()));
        } else if (vehicle instanceof Motorcycle moto) {

            // Aggiorna i campi specifici se il veicolo è una moto
            if (req.engineCc() != null) moto.setEngineCc(req.engineCc());
            if (req.motorcycleType() != null) moto.setMotorcycleType(MotorcycleType.valueOf(req.motorcycleType()));
        } else if (vehicle instanceof Van van) {

            // Aggiorna i campi specifici se il veicolo è un van
            if (req.cargoVolumeM3() != null) van.setCargoVolumeM3(req.cargoVolumeM3());
            if (req.maxLoadKg() != null) van.setMaxLoadKg(req.maxLoadKg());
        }

        Vehicle saved = vehicleRepository.save(vehicle);

        return VehicleResponse.from(saved);
    }

    // Elimina un veicolo tramite id
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veicolo non trovato con id " + id);
        }

        vehicleRepository.deleteById(id);
    }

    // Restituisce tutti i veicoli in versione paginata
    public Page<VehicleResponse> getAllPaged(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(VehicleResponse::from);
    }

    // Cerca un testo nella marca o nel modello ignorando maiuscole e minuscole
    public List<VehicleResponse> searchByText(String q) {
        return vehicleRepository.findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(q, q).stream().map(VehicleResponse::from).collect(Collectors.toList());
    }

    /*
     * Metodo factory.
     * Crea l'oggetto del tipo corretto in base al valore di vehicleType.
     */
    private Vehicle buildFromType(VehicleRequest req) {
        String type = req.vehicleType().toUpperCase();

        switch (type) {
            case "CAR" -> {
                Car car = new Car();
                if (req.numberOfDoors() != null) car.setNumberOfDoors(req.numberOfDoors());
                if (req.bodyType() != null) car.setBodyType(BodyType.valueOf(req.bodyType()));
                return car;
            }
            case "MOTORCYCLE" -> {
                Motorcycle moto = new Motorcycle();
                if (req.engineCc() != null) moto.setEngineCc(req.engineCc());
                if (req.motorcycleType() != null) moto.setMotorcycleType(MotorcycleType.valueOf(req.motorcycleType()));
                return moto;
            }
            case "VAN" -> {
                Van van = new Van();
                if (req.cargoVolumeM3() != null) van.setCargoVolumeM3(req.cargoVolumeM3());
                if (req.maxLoadKg() != null) van.setMaxLoadKg(req.maxLoadKg());
                return van;
            }
            default -> throw new BadRequestException("Tipo di veicolo non valido: " + req.vehicleType() + " (utilizza CAR, MOTORCYCLE o VAN)");
        }
    }

    /*
     * Imposta i campi comuni a tutti i veicoli.
     * Converte anche gli enum ricevuti come testo dal DTO.
     */
    private void fillCommonFields(Vehicle vehicle, VehicleRequest req) {
        vehicle.setBrand(req.brand());
        vehicle.setModel(req.model());
        vehicle.setYear(req.year());
        vehicle.setSeats(req.seats());
        vehicle.setPricePerDay(req.pricePerDay());
        vehicle.setLicensePlate(req.licensePlate());
        vehicle.setImage(req.image());
        vehicle.setDescription(req.description());

        try {
            vehicle.setCategory(VehicleCategory.valueOf(req.category()));
            vehicle.setTransmission(TransmissionType.valueOf(req.transmission()));
            vehicle.setFuel(FuelType.valueOf(req.fuel()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Categoria, cambio o carburante non validi");
        }
    }
}