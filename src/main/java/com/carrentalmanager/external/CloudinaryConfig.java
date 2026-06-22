package com.carrentalmanager.external;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * CloudinaryConfig è una classe di configurazione di Spring.
 * Configura il client Cloudinary usando le chiavi presenti in application.properties.
 *
 * Il metodo cloudinary() crea un Bean, cioè un oggetto gestito da Spring
 * e pronto per essere usato dal servizio che carica l'immagine del profilo.
 */
@Configuration
public class CloudinaryConfig {

    // Nome del cloud Cloudinary
    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    // Chiave pubblica API di Cloudinary
    @Value("${cloudinary.api-key}")
    private String apiKey;

    // Chiave segreta API di Cloudinary
    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    // Crea il client Cloudinary configurato
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret, "secure", true));
    }
}
