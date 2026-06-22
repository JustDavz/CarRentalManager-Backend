package com.carrentalmanager.service;

import com.carrentalmanager.dto.response.UserResponse;
import com.carrentalmanager.entity.User;
import com.carrentalmanager.exception.ResourceNotFoundException;
import com.carrentalmanager.external.CloudinaryService;
import com.carrentalmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/*
 * UserService è un service di Spring che contiene la logica legata all'utente.
 * Permette di recuperare il profilo dell'utente autenticato e di aggiornare
 * la sua immagine profilo.
 *
 * Per il caricamento dell'immagine usa CloudinaryService, che salva il file
 * su Cloudinary e restituisce l'URL pubblico dell'immagine.
 */
@Service
public class UserService {

    // Repository usato per recuperare e aggiornare gli utenti nel database
    private final UserRepository userRepository;

    // Service usato per caricare immagini su Cloudinary
    private final CloudinaryService cloudinaryService;

    // Dependency Injection tramite costruttore
    public UserService(UserRepository userRepository, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    // Restituisce il profilo dell'utente autenticato
    public UserResponse getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));

        return UserResponse.from(user);
    }

    /*
     * Aggiorna l'immagine profilo dell'utente autenticato.
     * Carica il file su Cloudinary, salva l'URL nel profilo utente
     * e restituisce i dati aggiornati.
     */
    public UserResponse updateProfileImage(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));

        // Carica l'immagine su Cloudinary nella cartella avatars
        String imageUrl = cloudinaryService.uploadImage(file, "avatars");

        // Salva l'URL dell'immagine nel profilo utente
        user.setProfileImage(imageUrl);
        userRepository.save(user);

        return UserResponse.from(user);
    }
}