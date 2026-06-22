package com.carrentalmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * CarRentalApiApplication è il punto di avvio dell'intera applicazione.
 * Da qui parte Spring Boot.
 *
 * All'avvio Spring avvia il server web, carica la configurazione,
 * scansiona le classi del progetto e registra componenti come controller,
 * service, repository, entity e configurazioni di sicurezza.
 *
 * @SpringBootApplication attiva la configurazione automatica di Spring Boot
 * e la scansione dei componenti a partire dal package com.carrentalmanager.
 */
@SpringBootApplication
public class CarRentalApiApplication {

    // Metodo main: avvia l'applicazione Spring Boot
    public static void main(String[] args) {
        SpringApplication.run(CarRentalApiApplication.class, args);
    }
}