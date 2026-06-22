package com.carrentalmanager.entity;

import com.carrentalmanager.entity.enums.BodyType;
import jakarta.persistence.*;

/*
 * Car è un'entità JPA (Java Persistence API) che rappresenta un'auto.
 * Estende Vehicle quindi eredita i campi comuni dei veicoli.
 *
 * Questa classe viene salvata nella tabella cars.
 */
@Entity
@Table(name = "cars")
public class Car extends Vehicle {

    // Numero di porte dell'auto
    @Column(name = "number_of_doors", nullable = false)
    private int numberOfDoors;

    // Tipo di carrozzeria dell'auto
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    // Restituisce il tipo concreto del veicolo
    @Override
    public String getVehicleType() {
        return "CAR";
    }

    // Getter e Setter
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(int numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }
}