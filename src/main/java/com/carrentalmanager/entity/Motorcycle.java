package com.carrentalmanager.entity;

import com.carrentalmanager.entity.enums.MotorcycleType;
import jakarta.persistence.*;

/*
 * Motorcycle è un'entità JPA (Java Persistence API) che rappresenta una moto.
 * Estende Vehicle, quindi eredita i campi comuni dei veicoli.
 *
 * Questa classe viene salvata nella tabella motorcycles.
 */
@Entity
@Table(name = "motorcycles")
public class Motorcycle extends Vehicle {

    // Cilindrata della moto
    @Column(name = "engine_cc", nullable = false)
    private int engineCc;

    // Tipologia della moto
    @Enumerated(EnumType.STRING)
    private MotorcycleType motorcycleType;

    // Restituisce il tipo concreto del veicolo
    @Override
    public String getVehicleType() {
        return "MOTORCYCLE";
    }

    // Getter e Setter

    public int getEngineCc() {
        return engineCc;
    }

    public void setEngineCc(int engineCc) {
        this.engineCc = engineCc;
    }

    public MotorcycleType getMotorcycleType() {
        return motorcycleType;
    }

    public void setMotorcycleType(MotorcycleType motorcycleType) {
        this.motorcycleType = motorcycleType;
    }
}