package com.carrentalmanager.external;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.carrentalmanager.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/*
 * CloudinaryService è un service di Spring usato per caricare immagini su Cloudinary.
 * Riceve un file, lo invia a Cloudinary e restituisce l'URL pubblico dell'immagine.
 *
 * Nel progetto viene usato per caricare e aggiornare l'immagine del profilo utente.
 */
@Service
public class CloudinaryService {

    // Client Cloudinary configurato nella classe CloudinaryConfig
    private final Cloudinary cloudinary;

    // Dependency Injection tramite costruttore
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    // Carica un'immagine su Cloudinary e restituisce l'URL sicuro https
    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File immagine mancante o vuoto");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folder));

            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new BadRequestException("Errore durante il caricamento dell'immagine");
        }
    }
}