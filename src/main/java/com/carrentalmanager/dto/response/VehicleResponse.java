package com.carrentalmanager.dto.response;

import com.carrentalmanager.entity.*;

import java.math.BigDecimal;

/*
 * VehicleResponse è un DTO di risposta, cioè rappresenta i dati di un veicolo
 * che il backend restituisce al client.
 *
 * Contiene i campi comuni del veicolo, come marca, modello, anno, categoria,
 * cambio, carburante, prezzo e disponibilità.
 *
 * Contiene anche i campi specifici del tipo di veicolo: auto, moto o van.
 * I campi non pertinenti al tipo di veicolo restano null.
 *
 * È un record: una classe immutabile e compatta usata come DTO, adatta a
 * trasferire i dati dal backend al client.
 *
 * Il metodo from() converte l'entità Vehicle in un oggetto VehicleResponse,
 * usando instanceof per capire il tipo del veicolo.
 */
public record VehicleResponse(
        Long id,
        String vehicleType,
        String brand,
        String model,
        int year,
        String category,
        String transmission,
        String fuel,
        int seats,
        BigDecimal pricePerDay,
        String licensePlate,
        String image,
        String description,
        boolean available,

        // Campi specifici. Null se non pertinenti al tipo di veicolo
        Integer numberOfDoors,
        String bodyType,
        Integer engineCc,
        String motorcycleType,
        BigDecimal cargoVolumeM3,
        Integer maxLoadKg
) {

    // Costruisce il DTO a partire dall'entità Vehicle
    public static VehicleResponse from(Vehicle vehicle) {
        Integer doors = null;
        String body = null;
        Integer cc = null;
        String motoType = null;
        BigDecimal cargo = null;
        Integer maxLoad = null;

        // In base al tipo di veicolo concreto recupera i campi specifici
        if (vehicle instanceof Car car) {
            doors = car.getNumberOfDoors();
            body = car.getBodyType() != null ? car.getBodyType().name() : null;
        } else if (vehicle instanceof Motorcycle moto) {
            cc = moto.getEngineCc();
            motoType = moto.getMotorcycleType() != null ? moto.getMotorcycleType().name() : null;
        } else if (vehicle instanceof Van van) {
            cargo = van.getCargoVolumeM3();
            maxLoad = van.getMaxLoadKg();
        }

        return new VehicleResponse(vehicle.getId(), vehicle.getVehicleType(), vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.getCategory().name(), vehicle.getTransmission().name(), vehicle.getFuel().name(), vehicle.getSeats(), vehicle.getPricePerDay(), vehicle.getLicensePlate(), vehicle.getImage(), vehicle.getDescription(), vehicle.isAvailable(), doors, body, cc, motoType, cargo, maxLoad);
    }
}