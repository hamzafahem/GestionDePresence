package ma.uh1.mpresence.config;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.entity.*;
import ma.uh1.mpresence.repository.*;
import ma.uh1.mpresence.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Seed de données de démo (profil "dev" avec H2) pour pouvoir tester l'API
 * et le back-office immédiatement sans base pré-remplie.
 *
 * Comptes de test :
 *  - Admin      : cin=admin      / password=admin123
 *  - Formateur  : cin=formateur1 / password=formateur123
 * Doctorant de démo : Code Apogée 8460304196 (mêmes valeurs que la carte
 * hardcodée dans hp/carte-d côté front, pour pouvoir tester le scan QR).
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;
    private final DoctorantRepository doctorantRepository;
    private final DemandeRepository demandeRepository;
    private final ReinscriptionRepository reinscriptionRepository;
    private final PublicationRechercheRepository publicationRepository;
    private final RendezVousRepository rendezVousRepository;
    private final NotificationRepository notificationRepository;
    private final FormationInterneRepository formationInterneRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        Permission manageFormations = save(new Permission(null, "MANAGE_FORMATIONS"));
        Permission viewFormations = save(new Permission(null, "VIEW_FORMATIONS"));
        Permission manageDoctorants = save(new Permission(null, "MANAGE_DOCTORANTS"));

        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        adminRole.setPermissions(Set.of(manageFormations, viewFormations, manageDoctorants));
        roleRepository.save(adminRole);

        Role formateurRole = new Role();
        formateurRole.setName("FORMATEUR");
        formateurRole.setPermissions(Set.of(viewFormations));
        roleRepository.save(formateurRole);

        User admin = new User();
        admin.setCin("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setNomFr("Maanaoui");
        admin.setPrenomFr("Hamza");
        admin.setEmail("admin@mpresence.local");
        admin.setAge(33);
        admin.setSexe("Homme");
        admin.setAddress("226, Avenue Ennil - Cite Djema, Casablanca 20430");
        admin.setPhone("+212613192620");
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        User formateur = new User();
        formateur.setCin("formateur1");
        formateur.setPassword(passwordEncoder.encode("formateur123"));
        formateur.setNomFr("Bennani");
        formateur.setPrenomFr("Youssef");
        formateur.setEmail("formateur1@mpresence.local");
        formateur.setRoles(Set.of(formateurRole));
        seedDemoPhoto(formateur);
        userRepository.save(formateur);

        AnneeUniversitaire annee = new AnneeUniversitaire();
        annee.setLibelle("2025-2026");
        annee.setDateDebut(LocalDate.of(2025, 9, 1));
        annee.setDateFin(LocalDate.of(2026, 7, 31));
        annee.setActive(true);
        anneeUniversitaireRepository.save(annee);

        // Pas de formation de démo pré-créée : à créer manuellement depuis le
        // back-office, puis affecter/inscrire les doctorants ci-dessous soi-même.

        Doctorant doctorant1 = new Doctorant();
        doctorant1.setNom("FAHEM");
        doctorant1.setPrenom("HAMZA");
        doctorant1.setCin("DJ280423");
        doctorant1.setCodeApogee("8460304196");
        doctorant1.setFiliere("Informatique");
        doctorant1.setPassword(passwordEncoder.encode("doctorant123"));
        doctorantRepository.save(doctorant1);

        Doctorant doctorant2 = new Doctorant();
        doctorant2.setNom("ALAMI");
        doctorant2.setPrenom("SARA");
        doctorant2.setCin("BK112233");
        doctorant2.setCne("N123456789");
        doctorant2.setCodeApogee("2109876543");
        doctorant2.setFiliere("Mathématiques");
        doctorant2.setPassword(passwordEncoder.encode("doctorant123"));
        doctorantRepository.save(doctorant2);

        Doctorant doctorant3 = new Doctorant();
        doctorant3.setNom("ALAOUI");
        doctorant3.setPrenom("IMANE");
        doctorant3.setCin("CD345678");
        doctorant3.setCne("7654321A098");
        doctorant3.setCodeApogee("7654321098");
        doctorant3.setFiliere("Physique");
        doctorant3.setStatut(StatutDoctorant.DIPLOME);
        doctorant3.setPassword(passwordEncoder.encode("doctorant123"));
        doctorantRepository.save(doctorant3);

        Demande demande = new Demande();
        demande.setDoctorant(doctorant1);
        demande.setType("ATTESTATION");
        demande.setObjet("Attestation de scolarité");
        demande.setDescription("Besoin d'une attestation pour un dossier de bourse.");
        demandeRepository.save(demande);

        Reinscription reinscription = new Reinscription();
        reinscription.setDoctorant(doctorant2);
        reinscription.setAnneeUniversitaire(annee);
        reinscriptionRepository.save(reinscription);

        PublicationRecherche article = new PublicationRecherche();
        article.setDoctorant(doctorant1);
        article.setType(TypePublication.ARTICLE);
        article.setTitre("Optimisation des réseaux de neurones profonds");
        article.setReference("Revue Marocaine d'Informatique");
        article.setDatePublication(LocalDate.of(2026, 2, 10));
        publicationRepository.save(article);

        RendezVous rdv = new RendezVous();
        rdv.setDoctorant(doctorant1);
        rdv.setFormateur(formateur);
        rdv.setObjet("Suivi d'avancement de thèse");
        rdv.setDateHeure(LocalDateTime.now().plusDays(3).withHour(10).withMinute(0));
        rdv.setLieu("Salle des thèses, UH1");
        rendezVousRepository.save(rdv);

        Notification notification = new Notification();
        notification.setTitre("Bienvenue");
        notification.setMessage("Bienvenue sur la plateforme CEDoc. Consultez vos formations et demandes.");
        notificationRepository.save(notification);

        FormationInterne formationInterne = new FormationInterne();
        formationInterne.setIntitule("Rédaction scientifique");
        formationInterne.setDescription("Atelier sur la rédaction d'articles scientifiques.");
        formationInterne.setFormateur(formateur);
        formationInterne.setDateDebut(LocalDate.of(2026, 3, 1));
        formationInterne.setDateFin(LocalDate.of(2026, 3, 2));
        formationInterne.setLieu("Amphi 3, UH1");
        formationInterne.setCapaciteMax(30);
        formationInterne.getParticipants().add(doctorant1);
        formationInterneRepository.save(formationInterne);
    }

    private Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    /**
     * Best-effort : préremplit la photo du formateur de démo depuis une image
     * locale au poste de dev. Silencieux si le fichier n'existe pas (autre
     * machine, autre utilisateur) — pas d'impact sur le reste du seed.
     */
    private void seedDemoPhoto(User formateur) {
        Path source = Path.of("C:\\Users\\Hamza Maanaoui\\Pictures\\Screenshots\\Capture d'écran 2026-09-08 142057.png");
        String url = fileStorageService.storeFromPath(source, "png");
        if (url != null) {
            formateur.setPhoto(url);
        }
    }
}
