package com.carrentalmanager.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/*
 * RentalService è un'entità JPA (Java Persistence API), cioè una classe Java collegata a una tabella del database.
 * Rappresenta un servizio aggiuntivo da aggiungere al noleggio come ad esempio assicurazione KASKO o soccorso stradale.
 *
 * Questa classe viene salvata nella tabella services.
 */
@Entity
@Table(name = "services")
public class RentalService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome del servizio
    @Column(nullable = false)
    private String name;

    // Descrizione del servizio
    @Column(columnDefinition = "TEXT")
    private String description;

    // Indica se il servizio è attivo
    @Column(nullable = false)
    private boolean active = true;

    // Lista dei prezzi del servizio in base alla categoria del veicolo
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicePrice> prices = new ArrayList<>();

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<ServicePrice> getPrices() {
        return prices;
    }

    public void setPrices(List<ServicePrice> prices) {
        this.prices = prices;
    }
}