package com.carrentalmanager.service;

import com.carrentalmanager.dto.request.BookingRequest;
import com.carrentalmanager.dto.response.BookingResponse;
import com.carrentalmanager.entity.*;
import com.carrentalmanager.entity.enums.BookingStatus;
import com.carrentalmanager.entity.enums.VehicleCategory;
import com.carrentalmanager.exception.BadRequestException;
import com.carrentalmanager.exception.ResourceNotFoundException;
import com.carrentalmanager.external.MailgunSender;
import com.carrentalmanager.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * BookingService è un service di Spring che contiene la logica delle prenotazioni.
 * Gestisce creazione, elenco, approvazione, rifiuto e chiusura delle prenotazioni.
 *
 * Durante la creazione controlla date, disponibilità del veicolo, servizi accessori
 * e calcola il prezzo totale del noleggio.
 *
 * All'approvazione invia anche un'email di conferma tramite Mailgun.
 */
@Service
public class BookingService {

    // Repository usato per gestire le prenotazioni nel database
    private final BookingRepository bookingRepository;

    // Repository usato per recuperare gli utenti
    private final UserRepository userRepository;

    // Repository usato per recuperare i veicoli
    private final VehicleRepository vehicleRepository;

    // Repository usato per recuperare le sedi di ritiro
    private final BranchRepository branchRepository;

    // Repository usato per recuperare i servizi accessori
    private final RentalServiceRepository serviceRepository;

    // Repository usato per recuperare il prezzo dei servizi in base alla categoria del veicolo
    private final ServicePriceRepository servicePriceRepository;

    // Service usato per inviare email tramite Mailgun
    private final MailgunSender mailgunSender;

    // Dependency Injection tramite costruttore
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, VehicleRepository vehicleRepository, BranchRepository branchRepository, RentalServiceRepository serviceRepository, ServicePriceRepository servicePriceRepository, MailgunSender mailgunSender) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.branchRepository = branchRepository;
        this.serviceRepository = serviceRepository;
        this.servicePriceRepository = servicePriceRepository;
        this.mailgunSender = mailgunSender;
    }

    /*
     * Crea una nuova prenotazione per l'utente autenticato.
     * Il prezzo totale viene calcolato:
     * (tariffa giornaliera veicolo + servizi scelti) X i giorni di noleggio.
     */
    public BookingResponse create(BookingRequest req, String userEmail) {

        // Recupera l'utente che sta prenotando
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));

        // Recupera il veicolo richiesto
        Vehicle vehicle = vehicleRepository.findById(req.vehicleId()).orElseThrow(() -> new ResourceNotFoundException("Veicolo non trovato con id " + req.vehicleId()));

        // Recupera la sede di ritiro
        Branch branch = branchRepository.findById(req.pickupBranchId()).orElseThrow(() -> new ResourceNotFoundException("Sede non trovata con id " + req.pickupBranchId()));

        // Controlla che la data di fine sia successiva alla data di inizio
        if (req.endDate().isBefore(req.startDate()) || req.endDate().isEqual(req.startDate())) {
            throw new BadRequestException("La data di fine deve essere successiva a quella di inizio");
        }

        // Impedisce prenotazioni con data di inizio nel passato
        if (req.startDate().isBefore(java.time.LocalDate.now())) {
            throw new BadRequestException("Non puoi prenotare per una data passata");
        }

        // Controlla che il veicolo sia disponibile
        if (!vehicle.isAvailable()) {
            throw new BadRequestException("Il veicolo non è disponibile");
        }

        // Calcola il numero di giorni di noleggio
        long days = ChronoUnit.DAYS.between(req.startDate(), req.endDate());

        // Parte dal prezzo giornaliero del veicolo
        BigDecimal dailyRate = vehicle.getPricePerDay();

        // Recupera la categoria del veicolo usata per calcolare il prezzo dei servizi
        VehicleCategory category = vehicle.getCategory();

        // Lista dei servizi accessori scelti dal cliente
        List<RentalService> chosenServices = new ArrayList<>();

        // Se il cliente ha scelto servizi accessori li aggiunge al prezzo giornaliero
        if (req.serviceIds() != null && !req.serviceIds().isEmpty()) {
            for (Long serviceId : req.serviceIds()) {

                // Recupera il servizio scelto
                RentalService service = serviceRepository.findById(serviceId).orElseThrow(() -> new ResourceNotFoundException("Servizio non trovato con id " + serviceId));

                // Recupera il prezzo del servizio per la categoria del veicolo
                ServicePrice price = servicePriceRepository.findByServiceIdAndCategory(serviceId, category).orElseThrow(() -> new BadRequestException("Prezzo non disponibile per il servizio '" + service.getName() + "' nella categoria " + category));

                // Aggiunge il prezzo giornaliero del servizio alla tariffa giornaliera totale
                dailyRate = dailyRate.add(price.getPricePerDay());

                // Aggiunge il servizio alla prenotazione
                chosenServices.add(service);
            }
        }

        // Calcola il prezzo totale del noleggio
        BigDecimal totalPrice = dailyRate.multiply(BigDecimal.valueOf(days));

        // Crea la nuova prenotazione
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setPickupBranch(branch);
        booking.setServices(chosenServices);
        booking.setStartDate(req.startDate());
        booking.setEndDate(req.endDate());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.IN_ATTESA);

        // Salva la prenotazione e restituisce il DTO di risposta
        Booking saved = bookingRepository.save(booking);

        return BookingResponse.from(saved);
    }

    // Restituisce tutte le prenotazioni per operatori e admin
    public List<BookingResponse> getAll() {
        return bookingRepository.findAll().stream().map(BookingResponse::from).collect(Collectors.toList());
    }

    // Restituisce tutte le prenotazioni in versione paginata
    public Page<BookingResponse> getAllPaged(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(BookingResponse::from);
    }

    // Restituisce le prenotazioni dell'utente autenticato
    public List<BookingResponse> getMyBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));

        return bookingRepository.findByUserId(user.getId()).stream().map(BookingResponse::from).collect(Collectors.toList());
    }

    /*
     * Approva una prenotazione.
     * Cambia lo stato in APPROVATO e invia un'email di conferma al cliente.
     */
    public BookingResponse approve(Long id) {
        Booking booking = findBookingOr404(id);
        booking.setStatus(BookingStatus.APPROVATO);
        booking.setRejectionReason(null);

        Booking saved = bookingRepository.save(booking);

        // Prepara l'email di conferma per il cliente
        String subject = "Prenotazione confermata - CarRentalManager";
        String text = "Ciao " + saved.getUser().getFirstName() + ",\n\n"
                + "la tua prenotazione #" + saved.getId() + " per "
                + saved.getVehicle().getBrand() + " " + saved.getVehicle().getModel()
                + " e' stata APPROVATA.\n"
                + "Periodo: dal " + saved.getStartDate() + " al " + saved.getEndDate() + ".\n"
                + "Totale: " + saved.getTotalPrice() + " euro.\n"
                + "Ritiro presso: " + saved.getPickupBranch().getName() + ".\n\n"
                + "Grazie per aver scelto CarRentalManager!";

        // Invia l'email tramite Mailgun
        mailgunSender.sendEmail(saved.getUser().getEmail(), subject, text);

        return BookingResponse.from(saved);
    }

    /*
     * Rifiuto di una prenotazione.
     * Cambia lo stato in RIFIUTA e invia un'email di rifiuto noleggio al cliente.
     */
    public BookingResponse reject(Long id, String reason) {
        Booking booking = findBookingOr404(id);
        booking.setStatus(BookingStatus.RIFIUTATO);
        booking.setRejectionReason(reason);

        Booking saved = bookingRepository.save(booking);

        // Prepara l'email di notifica del rifiuto per il cliente
        String subject = "Prenotazione rifiutata - CarRentalManager";
        String text = "Ciao " + saved.getUser().getFirstName() + ",\n\n"
                + "siamo spiacenti di comunicarti che la tua prenotazione #" + saved.getId() + " per "
                + saved.getVehicle().getBrand() + " " + saved.getVehicle().getModel()
                + " e' stata RIFIUTATA.\n"
                + "Motivazione: " + saved.getRejectionReason() + "\n\n"
                + "Per qualsiasi chiarimento puoi contattare il nostro servizio clienti.\n"
                + "Grazie per aver scelto CarRentalManager!";

        // Invia l'email tramite Mailgun
        mailgunSender.sendEmail(saved.getUser().getEmail(), subject, text);

        return BookingResponse.from(saved);
    }

    /*
     * Chiude una prenotazione a noleggio concluso.
     * Può essere chiusa solo una prenotazione già approvata. Al termine del noleggio verrà inviata un'email al cliente. 
     */
    public BookingResponse close(Long id) {
        Booking booking = findBookingOr404(id);

        if (booking.getStatus() != BookingStatus.APPROVATO) {
            throw new BadRequestException("Si puo' chiudere solo una prenotazione approvata");
        }

        booking.setStatus(BookingStatus.CHIUSO);

        Booking saved = bookingRepository.save(booking);

        // Invia un'email di chiusura noleggio al cliente (API esterna: Mailgun)
        String subject = "Noleggio concluso - CarRentalManager";
        String text = "Ciao " + saved.getUser().getFirstName() + ",\n\n"
                + "il noleggio relativo alla prenotazione #" + saved.getId() + " per "
                + saved.getVehicle().getBrand() + " " + saved.getVehicle().getModel()
                + " e' stato concluso.\n"
                + "Grazie per aver scelto CarRentalManager, speriamo di rivederti presto!";
        mailgunSender.sendEmail(saved.getUser().getEmail(), subject, text);

        return BookingResponse.from(saved);
    }

    // Recupera una prenotazione tramite id oppure lancia errore 404
    private Booking findBookingOr404(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prenotazione non trovata con id " + id));
    }
}