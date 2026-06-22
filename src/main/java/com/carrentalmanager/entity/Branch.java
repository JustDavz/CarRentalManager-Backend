package com.carrentalmanager.entity;

import jakarta.persistence.*;

/*
 * Branch è un'entità JPA (Java Persistence API), cioè una classe collegata a una tabella del database.
 * Rappresenta una sede o filiale dove si ritira o riconsegna il veicolo.
 *
 * Questa classe viene salvata nella tabella branches.
 */
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome della sede
    @Column(nullable = false)
    private String name;

    // Indirizzo della sede
    @Column(nullable = false)
    private String address;

    // Numero di telefono della sede
    @Column(nullable = false)
    private String phone;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
