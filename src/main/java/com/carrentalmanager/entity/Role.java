package com.carrentalmanager.entity;

import com.carrentalmanager.entity.enums.RoleName;
import jakarta.persistence.*;

/*
 * Role è un'entità JPA (Java Persistence API), cioè una classe Java collegata a una tabella del database.
 * Rappresenta un ruolo che può essere assegnato agli utenti (ADMIN, EMPLOYEE, CUSTOMER)
 *
 * Questa classe viene salvata nella tabella roles.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome del ruolo salvato come testo nel database
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}