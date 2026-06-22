package com.carrentalmanager.service;

import com.carrentalmanager.dto.request.LoginRequest;
import com.carrentalmanager.dto.request.RegisterRequest;
import com.carrentalmanager.dto.response.AuthResponse;
import com.carrentalmanager.dto.response.UserResponse;
import com.carrentalmanager.entity.Role;
import com.carrentalmanager.entity.User;
import com.carrentalmanager.entity.enums.RoleName;
import com.carrentalmanager.exception.BadRequestException;
import com.carrentalmanager.external.MailgunSender;
import com.carrentalmanager.repository.RoleRepository;
import com.carrentalmanager.repository.UserRepository;
import com.carrentalmanager.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
 * AuthService è un service di Spring che contiene la logica di autenticazione.
 * Gestisce registrazione, login e recupero password.
 *
 * Usa i repository per leggere e salvare utenti e ruoli, BCrypt per cifrare
 * le password, JwtService per generare token JWT e Mailgun per inviare email.
 */
@Service
public class AuthService {

    // Repository usato per gestire gli utenti nel database
    private final UserRepository userRepository;

    // Repository usato per recuperare i ruoli dal database
    private final RoleRepository roleRepository;

    // Encoder usato per cifrare e verificare le password
    private final PasswordEncoder passwordEncoder;

    // Service usato per generare e validare token JWT
    private final JwtService jwtService;

    // Oggetto di Spring Security usato per autenticare email e password
    private final AuthenticationManager authManager;

    // Service usato per inviare email tramite Mailgun
    private final MailgunSender mailgunSender;

    // Dependency Injection tramite costruttore
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authManager, MailgunSender mailgunSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.mailgunSender = mailgunSender;
    }

    /*
     * Registra un nuovo utente.
     * Controlla che l'email non sia già usata, cifra la password,
     * assegna il ruolo CUSTOMER e restituisce token JWT e dati utente.
     */
    public AuthResponse register(RegisterRequest req) {

        // Verifica che l'email non sia già registrata
        if (userRepository.existsByEmail(req.email())) {
            throw new BadRequestException("Email gia' registrata");
        }

        // Crea il nuovo utente e imposta i dati ricevuti dal DTO
        User user = new User();
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setPhone(req.phone());

        // Genera un avatar automatico usando nome e cognome
        user.setProfileImage(generateAvatarUrl(req.firstName(), req.lastName()));

        // Recupera il ruolo CUSTOMER dal database
        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER).orElseThrow(() -> new BadRequestException("Ruolo CUSTOMER non trovato"));

        // Assegna il ruolo CUSTOMER al nuovo utente
        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);
        user.setRoles(roles);

        // Salva l'utente nel database
        userRepository.save(user);

        // Invia un'email di benvenuto al nuovo utente (API esterna: Mailgun)
        String subject = "Benvenuto in CarRentalManager!";
        String text = "Ciao " + user.getFirstName() + ",\n\n"
                + "il tuo account su CarRentalManager e' stato creato con successo.\n"
                + "Da ora puoi sfogliare il catalogo veicoli e prenotare il tuo noleggio.\n\n"
                + "Grazie per esserti registrato!";
        mailgunSender.sendEmail(user.getEmail(), subject, text);

        // Genera il token JWT e restituisce la risposta al client
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, UserResponse.from(user));
    }

    /*
     * Effettua il login di un utente esistente.
     * Se email e password sono corrette, genera un token JWT.
     */
    public AuthResponse login(LoginRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        } catch (AuthenticationException e) {
            throw new BadRequestException("Email o password non corretti");
        }

        // Recupera l'utente autenticato dal database
        User user = userRepository.findByEmail(req.email()).orElseThrow(() -> new BadRequestException("Utente non trovato"));

        // Genera il token JWT e restituisce token e dati utente
        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, UserResponse.from(user));
    }

    /*
     * Avvia la procedura di recupero password.
     * Genera un token temporaneo e lo salva sull'utente e lo invia via email.
     *
     * Per sicurezza non rivela se l'email esiste oppure no.
     */
    public void forgotPassword(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {

            // Genera un token univoco con scadenza di un'ora
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            // Prepara e invia l'email di recupero password
            String subject = "Recupero password - CarRentalManager";
            String text = "Ciao " + user.getFirstName() + ",\n\n"
                    + "hai richiesto il reset della password.\n"
                    + "Usa questo codice per impostarne una nuova:\n\n"
                    + token + "\n\n"
                    + "Il codice scade tra 1 ora. Se non hai richiesto tu il reset, ignora questa email.";

            mailgunSender.sendEmail(user.getEmail(), subject, text);
        });
    }

    /*
     * Completa il reset della password.
     * Verifica che il token esista e non sia scaduto, poi salva la nuova password cifrata.
     */
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token).orElseThrow(() -> new BadRequestException("Token non valido"));

        // Verifica che il token non sia scaduto
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token scaduto, richiedine uno nuovo");
        }

        // Salva la nuova password cifrata e rimuove il token usato
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    /*
     * Genera l'URL di un avatar automatico tramite il servizio esterno API ui-avatars.com.
     * Usa nome e cognome dell'utente per creare l'immagine iniziale del profilo.
     */
    private String generateAvatarUrl(String firstName, String lastName) {
        String name = (firstName + "+" + lastName).replace(" ", "+");

        return "https://ui-avatars.com/api/?name=" + name + "&background=random&size=256&bold=true";
    }
}