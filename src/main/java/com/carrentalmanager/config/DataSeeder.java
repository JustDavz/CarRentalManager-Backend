package com.carrentalmanager.config;

import com.carrentalmanager.entity.*;
import com.carrentalmanager.entity.enums.*;
import com.carrentalmanager.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Seeder dei dati demo.
 * All'avvio dell'applicazione inserisce ruoli, utenti, sedi, veicoli,
 * servizi aggiuntivi e alcune prenotazioni di esempio.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    // Repository e utility necessari per salvare i dati demo
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final BranchRepository branchRepository;
    private final RentalServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;
    private final ServicePriceRepository servicePriceRepository;
    private final PasswordEncoder passwordEncoder;

    // Dependency Injection tramite costruttore
    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, VehicleRepository vehicleRepository, BranchRepository branchRepository, RentalServiceRepository serviceRepository, BookingRepository bookingRepository, ServicePriceRepository servicePriceRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.branchRepository = branchRepository;
        this.serviceRepository = serviceRepository;
        this.bookingRepository = bookingRepository;
        this.servicePriceRepository = servicePriceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Metodo eseguito automaticamente all'avvio dell'applicazione.
    @Override
    public void run(String... args) {

        // Evita di inserire dati duplicati
        if (roleRepository.count() > 0) {
            return;
        }

        // Ruoli
        Role admin = newRole(RoleName.ADMIN);
        Role employee = newRole(RoleName.EMPLOYEE);
        Role customer = newRole(RoleName.CUSTOMER);

        // Utenti demo
        createUser("admin@carrental.it", "David", "Conocchioli", Set.of(admin));
        createUser("operatore@carrental.it", "Luca", "Bianchi", Set.of(employee));
        createUser("david.conocchioli@gmail.com", "David", "Conocchioli", Set.of(customer));

        // Sedi
        createBranch("Roma Centro", "Via del Corso, 120 — 00186 ROMA (RM)", "+39 06 1111111");
        createBranch("Roma Nord", "Via Cassia, 410 — 00189 ROMA (RM)", "+39 06 2222222");
        createBranch("Roma Sud", "Via Cristoforo Colombo, 285 — 00147 ROMA (RM)", "+39 06 3333333");

        // Auto basic
        saveCar("Fiat", "500", 2025, VehicleCategory.BASIC, TransmissionType.MANUALE, FuelType.IBRIDO, 4, "90.00", "GZ923IU", 3, BodyType.CITY_CAR);
        saveCar("Fiat", "Pandina", 2026, VehicleCategory.BASIC, TransmissionType.MANUALE, FuelType.IBRIDO, 4, "60.00", "HA231JK", 5, BodyType.CITY_CAR);
        saveCar("Volkswagen", "Polo", 2019, VehicleCategory.BASIC, TransmissionType.MANUALE, FuelType.DIESEL, 4, "70.00", "FZ624CC", 5, BodyType.BERLINA);
        saveCar("Toyota", "Aygo", 2026, VehicleCategory.BASIC, TransmissionType.MANUALE, FuelType.BENZINA, 4, "110.00", "HA399IX", 3, BodyType.CITY_CAR);
        saveCar("Jeep", "Renegade", 2025, VehicleCategory.BASIC, TransmissionType.AUTOMATICA, FuelType.ELETTRICO, 5, "130.00", "GT354YX", 5, BodyType.SUV);

        // Auto premium
        saveCar("Audi", "RS3", 2026, VehicleCategory.PREMIUM, TransmissionType.AUTOMATICA, FuelType.BENZINA, 5, "600.00", "HA000HB", 5, BodyType.BERLINA);
        saveCar("BMW", "Serie 3", 2024, VehicleCategory.PREMIUM, TransmissionType.AUTOMATICA, FuelType.BENZINA, 5, "250.00", "GS111YT", 5, BodyType.BERLINA);
        saveCar("Mercedes-Benz", "GLC", 2025, VehicleCategory.PREMIUM, TransmissionType.AUTOMATICA, FuelType.IBRIDO, 5, "400.00", "GR227LA", 5, BodyType.SUV);
        saveCar("Tesla", "Model 3", 2025, VehicleCategory.PREMIUM, TransmissionType.AUTOMATICA, FuelType.ELETTRICO, 5, "350.00", "GZ524DM", 4, BodyType.BERLINA);
        saveCar("Volkswagen", "Golf GTI", 2025, VehicleCategory.PREMIUM, TransmissionType.AUTOMATICA, FuelType.BENZINA, 5, "450.00", "GT222KK", 5, BodyType.BERLINA);

        // Auto luxury
        saveCar("Ferrari", "296 GTB", 2026, VehicleCategory.LUXURY, TransmissionType.AUTOMATICA, FuelType.IBRIDO, 2, "1800.00", "HA296GT", 2, BodyType.COUPE);
        saveCar("Lamborghini", "Revuelto", 2024, VehicleCategory.LUXURY, TransmissionType.AUTOMATICA, FuelType.IBRIDO, 2, "1600.00", "GR729UZ", 2, BodyType.COUPE);
        saveCar("Porsche", "GT3 RS", 2024, VehicleCategory.LUXURY, TransmissionType.AUTOMATICA, FuelType.BENZINA, 2, "1500.00", "GH223KL", 2, BodyType.COUPE);
        saveCar("Lamborghini", "Urus", 2026, VehicleCategory.LUXURY, TransmissionType.AUTOMATICA, FuelType.IBRIDO, 5, "1100.00", "HA111GS", 5, BodyType.SUV);

        // Moto e scooter
        saveMoto("Ducati", "Panigale V4 S", 2026, VehicleCategory.LUXURY, FuelType.BENZINA, "150.00", "FA111AA", 1103, MotorcycleType.SPORT);
        saveMoto("Honda", "SH 350", 2025, VehicleCategory.BASIC, FuelType.BENZINA, "30.00", "FA222BB", 330, MotorcycleType.SCOOTER);

        // Van
        saveVan("Ford", "Transit", 2022, VehicleCategory.BASIC, FuelType.DIESEL, 3, "70.00", "EF666FF", "11.00", 1200);
        saveVan("Mercedes-Benz", "Sprinter", 2023, VehicleCategory.PREMIUM, FuelType.DIESEL, 3, "95.00", "EH888HH", "14.00", 1500);

        // Servizi aggiuntivi con prezzi per categoria
        createServiceWithPrices("Assicurazione KASKO", new BigDecimal("100.00"), new BigDecimal("200.00"), new BigDecimal("300.00"));
        createServiceWithPrices("Soccorso Stradale Premium", new BigDecimal("50.00"), new BigDecimal("50.00"), new BigDecimal("50.00"));
        createServiceWithPrices("Conducente Aggiuntivo", new BigDecimal("20.00"), new BigDecimal("50.00"), new BigDecimal("250.00"));
        createServiceWithPrices("Seggiolino Bambino", new BigDecimal("10.00"), new BigDecimal("10.00"), new BigDecimal("10.00"));

        // Prenotazioni demo per testare dashboard e report
        seedDemoBookings();

        System.out.println("Dati demo inseriti con successo...");
    }

    // Crea e salva un ruolo
    private Role newRole(RoleName name) {
        Role role = new Role();
        role.setName(name);

        return roleRepository.save(role);
    }

    // Crea e salva un utente demo
    private void createUser(String email, String first, String last, Set<Role> roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName(first);
        user.setLastName(last);
        user.setRoles(roles);

        userRepository.save(user);
    }

    // Crea e salva una sede
    private void createBranch(String name, String address, String phone) {
        Branch branch = new Branch();
        branch.setName(name);
        branch.setAddress(address);
        branch.setPhone(phone);

        branchRepository.save(branch);
    }

    // Compila i campi comuni a tutti i veicoli
    private void fillVehicle(Vehicle vehicle, String brand, String model, int year, VehicleCategory category, TransmissionType transmission, FuelType fuel, int seats, BigDecimal price, String plate) {
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setCategory(category);
        vehicle.setTransmission(transmission);
        vehicle.setFuel(fuel);
        vehicle.setSeats(seats);
        vehicle.setPricePerDay(price);
        vehicle.setLicensePlate(plate);
        vehicle.setAvailable(true);
    }

    // Crea e salva un'auto
    private void saveCar(String brand, String model, int year, VehicleCategory category, TransmissionType transmission, FuelType fuel, int seats, String price, String plate, int doors, BodyType body) {
        Car car = new Car();

        fillVehicle(car, brand, model, year, category, transmission, fuel, seats, new BigDecimal(price), plate);

        car.setNumberOfDoors(doors);
        car.setBodyType(body);

        vehicleRepository.save(car);
    }

    // Crea e salva una moto
    private void saveMoto(String brand, String model, int year, VehicleCategory category, FuelType fuel, String price, String plate, int cc, MotorcycleType type) {
        Motorcycle motorcycle = new Motorcycle();

        fillVehicle(motorcycle, brand, model, year, category, TransmissionType.MANUALE, fuel, 2, new BigDecimal(price), plate);

        motorcycle.setEngineCc(cc);
        motorcycle.setMotorcycleType(type);

        vehicleRepository.save(motorcycle);
    }

    //Crea e salva un van
    private void saveVan(String brand, String model, int year, VehicleCategory category, FuelType fuel, int seats, String price, String plate, String cargoM3, int maxLoad) {
        Van van = new Van();

        fillVehicle(van, brand, model, year, category, TransmissionType.MANUALE, fuel, seats, new BigDecimal(price), plate);

        van.setCargoVolumeM3(new BigDecimal(cargoM3));
        van.setMaxLoadKg(maxLoad);

        vehicleRepository.save(van);
    }

    // Crea un servizio aggiuntivo con i prezzi per categoria.
    private void createServiceWithPrices(String name, BigDecimal basic, BigDecimal premium, BigDecimal luxury) {
        RentalService service = new RentalService();
        service.setName(name);
        service.setActive(true);

        ServicePrice basicServicePrice = makePrice(service, VehicleCategory.BASIC, basic);
        ServicePrice premiumServicePrice = makePrice(service, VehicleCategory.PREMIUM, premium);
        ServicePrice luxuryServicePrice = makePrice(service, VehicleCategory.LUXURY, luxury);

        service.setPrices(List.of(basicServicePrice, premiumServicePrice, luxuryServicePrice));

        serviceRepository.save(service);
    }

    // Crea il prezzo di un servizio per una specifica categoria.
    private ServicePrice makePrice(RentalService service, VehicleCategory category, BigDecimal price) {
        ServicePrice servicePrice = new ServicePrice();
        servicePrice.setService(service);
        servicePrice.setCategory(category);
        servicePrice.setPricePerDay(price);

        return servicePrice;
    }

    // Crea alcune prenotazioni demo per il cliente
    private void seedDemoBookings() {
        User customer = userRepository.findByEmail("david.conocchioli@gmail.com").orElse(null);

        if (customer == null) {
            return;
        }

        List<Branch> branches = branchRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<RentalService> services = serviceRepository.findAll();

        if (branches.isEmpty() || vehicles.isEmpty()) {
            return;
        }

        Branch sede = branches.get(0);

        createDemoBooking(customer, vehicles.get(0), sede, LocalDate.now().plusDays(3), LocalDate.now().plusDays(6), List.of(services.get(0), services.get(1)), BookingStatus.IN_ATTESA, null);
        createDemoBooking(customer, vehicles.get(5), sede, LocalDate.now().plusDays(10), LocalDate.now().plusDays(12), List.of(), BookingStatus.APPROVATO, null);
        createDemoBooking(customer, vehicles.get(10), sede, LocalDate.now().minusDays(10), LocalDate.now().minusDays(7), List.of(services.get(0)), BookingStatus.CHIUSO, null);
        createDemoBooking(customer, vehicles.get(14), sede, LocalDate.now().plusDays(15), LocalDate.now().plusDays(17), List.of(services.get(0)), BookingStatus.IN_ATTESA, null);
        createDemoBooking(customer, vehicles.get(16), sede, LocalDate.now().plusDays(20), LocalDate.now().plusDays(22), List.of(), BookingStatus.RIFIUTATO, "Veicolo non disponibile nel periodo richiesto");
    }

    // Crea e salva una prenotazione demo calcolando il prezzo totale.
    private void createDemoBooking(User user, Vehicle vehicle, Branch branch, LocalDate start, LocalDate end, List<RentalService> chosenServices, BookingStatus status, String rejectionReason) {
        long days = ChronoUnit.DAYS.between(start, end);

        if (days <= 0) {
            days = 1;
        }

        BigDecimal dailyRate = vehicle.getPricePerDay();
        List<RentalService> servicesToSave = new ArrayList<>();

        for (RentalService service : chosenServices) {
            ServicePrice price = servicePriceRepository.findByServiceIdAndCategory(service.getId(), vehicle.getCategory()).orElse(null);

            if (price != null) {
                dailyRate = dailyRate.add(price.getPricePerDay());
                servicesToSave.add(service);
            }
        }

        BigDecimal totalPrice = dailyRate.multiply(BigDecimal.valueOf(days));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setPickupBranch(branch);
        booking.setServices(servicesToSave);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(status);
        booking.setRejectionReason(rejectionReason);

        bookingRepository.save(booking);
    }
}