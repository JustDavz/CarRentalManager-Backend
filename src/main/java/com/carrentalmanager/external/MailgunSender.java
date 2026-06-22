package com.carrentalmanager.external;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*
 * MailgunSender è un service di Spring usato per inviare email tramite l'API di Mailgun.
 * Legge le credenziali da application.properties e usa OkHttp per eseguire una richiesta POST autenticata.
 *
 * Nel progetto viene usato per inviare email come reset password, conferme o notifiche sulle prenotazioni.
 */
@Service
public class MailgunSender {

    // Dominio Mailgun configurato in application.properties
    @Value("${mailgun.domain}")
    private String domain;

    // API key di Mailgun
    @Value("${mailgun.api-key}")
    private String apiKey;

    // Mittente delle email
    @Value("${mailgun.sender}")
    private String sender;

    // Client HTTP usato per chiamare l'API di Mailgun
    private final OkHttpClient client = new OkHttpClient();

    // Invia un'email al destinatario indicato
    public void sendEmail(String to, String subject, String text) {
        try {
            // Endpoint Mailgun del dominio configurato
            String url = "https://api.mailgun.net/v3/" + domain + "/messages";

            // Corpo della richiesta con mittente, destinatario, oggetto e testo
            RequestBody body = new FormBody.Builder().add("from", "CarRentalManager <" + sender + ">").add("to", to).add("subject", subject).add("text", text).build();

            // Credenziali per autenticare la richiesta
            String credential = Credentials.basic("api", apiKey);

            // Richiesta HTTP POST verso Mailgun
            Request request = new Request.Builder().url(url).post(body).header("Authorization", credential).build();

            // Esegue la chiamata e chiude automaticamente la risposta
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Email inviata a " + to);
                } else {
                    System.out.println("Invio email fallito: " + response.code() + " " + response.message());
                }
            }
        } catch (Exception e) {
            System.out.println("Errore invio email: " + e.getMessage());
        }
    }
}