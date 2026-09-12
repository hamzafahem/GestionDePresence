package ma.uh1.mpresence.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Stockage local des photos de profil (uploads/photos/ à la racine du
 * projet). Les fichiers sont servis publiquement via /uploads/** (voir
 * WebConfig) car une balise &lt;img&gt; ne peut pas envoyer de Bearer token.
 */
@Service
public class FileStorageService {

    private final Path uploadDir;
    private final String baseUrl;

    public FileStorageService(
            @Value("${app.uploads.dir}") String uploadsDir,
            @Value("${app.base-url}") String baseUrl
    ) {
        this.uploadDir = Paths.get(uploadsDir);
        this.baseUrl = baseUrl;
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de créer le dossier d'uploads: " + this.uploadDir, e);
        }
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide");
        }

        String extension = extensionOf(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
        Path target = uploadDir.resolve(filename);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Échec de l'enregistrement du fichier", e);
        }

        return baseUrl + "/api/uploads/photos/" + filename;
    }

    /**
     * Copie un fichier local (hors upload HTTP) vers le dossier d'uploads —
     * utilisé par DataSeeder pour préremplir une photo de démo, en best-effort
     * (le chemin source n'existe pas forcément sur toutes les machines).
     */
    public String storeFromPath(Path source, String extension) {
        if (source == null || !Files.exists(source)) {
            return null;
        }
        String filename = UUID.randomUUID() + (extension == null || extension.isEmpty() ? "" : "." + extension);
        Path target = uploadDir.resolve(filename);
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return null;
        }
        return baseUrl + "/api/uploads/photos/" + filename;
    }

    public void delete(String url) {
        if (url == null) {
            return;
        }
        String marker = "/uploads/photos/";
        int idx = url.indexOf(marker);
        if (idx < 0) {
            return;
        }
        String filename = url.substring(idx + marker.length());
        try {
            Files.deleteIfExists(uploadDir.resolve(filename));
        } catch (IOException ignored) {
            // best-effort : un fichier déjà absent n'est pas une erreur
        }
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        int dot = originalFilename.lastIndexOf('.');
        return dot >= 0 ? originalFilename.substring(dot + 1) : "";
    }
}
