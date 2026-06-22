package com.carrentalmanager.controller;

import com.carrentalmanager.dto.response.UserResponse;
import com.carrentalmanager.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/*
 * Controller REST per la gestione del profilo utente.
 * Permette di leggere il profilo e aggiornare l'immagine personale.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    // Service che contiene la logica del profilo utente
    private final UserService userService;

    // Dependency Injection tramite costruttore
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Restituisce il profilo dell'utente autenticato
    @GetMapping("/me")
    public UserResponse getMyProfile(Authentication authentication) {
        return userService.getProfile(authentication.getName());
    }

    // Carica o aggiorna la foto profilo dell'utente autenticato
    @PostMapping("/me/avatar")
    public UserResponse uploadAvatar(@RequestParam("file") MultipartFile file, Authentication authentication) {
        return userService.updateProfileImage(authentication.getName(), file);
    }
}