package com.carrentalmanager.entity;

import com.carrentalmanager.entity.enums.VehicleCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;

/*
 * ServicePrice è un'entità JPA (Java Persistence API), cioè una classe Java collegata a una tabella del database.
 * Rappresenta il prezzo giornaliero di un servizio per una specifica categoria di veicolo.
 *
 * Questa classe viene salvata nella tabella service_prices.
 */
@Entity
@Table(name = "service_prices")
public class ServicePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Servizio a cui appartiene questo prezzo
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private RentalService service;

    // Categoria del veicolo a cui si applica il prezzo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleCategory category;

    // Prezzo giornaliero del servizio
    @Column(name = "price_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RentalService getService() {
        return service;
    }

    public void setService(RentalService service) {
        this.service = service;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}