package com.carrentalmanager.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

/*
 * Van è un'entità JPA (Java Persistence API), cioè una classe Java collegata a una tabella del database.
 * Rappresenta un van da carico e estende Vehicle quindi eredita i campi comuni dei veicoli.
 *
 * Questa classe viene salvata nella tabella vans.
 */
@Entity
@Table(name = "vans")
public class Van extends Vehicle {

    // Volume di carico del van espresso in metri cubi
    @Column(name = "cargo_volume_m3", precision = 6, scale = 2)
    private BigDecimal cargoVolumeM3;

    // Portata massima del van espressa in kg
    @Column(name = "max_load_kg")
    private Integer maxLoadKg;

    // Restituisce il tipo concreto del veicolo
    @Override
    public String getVehicleType() {
        return "VAN";
    }

    // Getter e Setter
    public BigDecimal getCargoVolumeM3() {
        return cargoVolumeM3;
    }

    public void setCargoVolumeM3(BigDecimal cargoVolumeM3) {
        this.cargoVolumeM3 = cargoVolumeM3;
    }

    public Integer getMaxLoadKg() {
        return maxLoadKg;
    }

    public void setMaxLoadKg(Integer maxLoadKg) {
        this.maxLoadKg = maxLoadKg;
    }
}