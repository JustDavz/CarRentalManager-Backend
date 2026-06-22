package com.carrentalmanager.entity;

import com.carrentalmanager.entity.enums.FuelType;
import com.carrentalmanager.entity.enums.TransmissionType;
import com.carrentalmanager.entity.enums.VehicleCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;

/*
 * Vehicle è un'entità JPA (Java Persistence API), cioè una classe Java collegata a una tabella del database.
 * È la classe padre astratta di tutti i veicoli: auto, moto e van.
 *
 * Contiene i campi comuni a tutti i veicoli, come marca, modello, anno,
 * categoria, cambio, carburante, posti, prezzo, targa e disponibilità.
 *
 * Usa l'ereditarietà JOINED: i campi comuni vengono salvati nella tabella vehicles,
 * mentre i campi specifici vengono salvati nelle tabelle figlie.
 */
@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Marca del veicolo
    @Column(nullable = false)
    private String brand;

    // Modello del veicolo
    @Column(nullable = false)
    private String model;

    // Anno del veicolo
    @Column(nullable = false)
    private int year;

    // Categoria del veicolo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleCategory category;

    // Tipo di cambio del veicolo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransmissionType transmission;

    // Tipo di alimentazione del veicolo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuel;

    // Numero di posti del veicolo
    @Column(nullable = false)
    private int seats;

    // Prezzo giornaliero del noleggio
    @Column(name = "price_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    // Targa del veicolo
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    // Immagine del veicolo
    private String image;

    // Descrizione del veicolo
    @Column(columnDefinition = "TEXT")
    private String description;

    // Disponibilità del veicolo
    @Column(nullable = false)
    private boolean available = true;

    // Ogni classe figlia restituisce il proprio tipo concreto
    public abstract String getVehicleType();

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }

    public TransmissionType getTransmission() {
        return transmission;
    }

    public void setTransmission(TransmissionType transmission) {
        this.transmission = transmission;
    }

    public FuelType getFuel() {
        return fuel;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}