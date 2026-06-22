package com.carrentalmanager.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/*
 * User è un'entità JPA (Java Persistence API), cioè una classe Java collegata a una tabella del database.
 * Rappresenta un utente registrato del sistema.
 *
 * Contiene email, password, dati anagrafici, immagine profilo, data di registrazione,
 * dati per il recupero password e ruoli associati.
 *
 * Questa classe viene salvata nella tabella users.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email dell'utente, obbligatoria e univoca
    @Column(nullable = false, unique = true)
    private String email;

    // Password cifrata
    @Column(nullable = false)
    private String password;

    // Nome dell'utente
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // Cognome dell'utente
    @Column(name = "last_name", nullable = false)
    private String lastName;

    // Numero di telefono dell'utente
    private String phone;

    // URL dell'immagine profilo
    @Column(name = "profile_image")
    private String profileImage;

    // Data di registrazione dell'utente
    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    // Token usato per il recupero password
    @Column(name = "reset_token")
    private String resetToken;

    // Scadenza del token di recupero password
    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    // Ruoli associati all'utente
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Imposta automaticamente la data di registrazione al primo salvataggio
    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}