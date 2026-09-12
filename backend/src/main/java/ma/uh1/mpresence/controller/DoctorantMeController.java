package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ChangePasswordRequest;
import ma.uh1.mpresence.dto.DemandeDto;
import ma.uh1.mpresence.dto.DoctorantDto;
import ma.uh1.mpresence.dto.FormationListResponse;
import ma.uh1.mpresence.dto.InscriptionCreateRequest;
import ma.uh1.mpresence.dto.InscriptionDto;
import ma.uh1.mpresence.dto.MesParticipationDto;
import ma.uh1.mpresence.dto.ReclamationCreateRequest;
import ma.uh1.mpresence.dto.UserSettingsDto;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.service.DemandeService;
import ma.uh1.mpresence.service.DoctorantService;
import ma.uh1.mpresence.service.FormationService;
import ma.uh1.mpresence.service.InscriptionService;
import ma.uh1.mpresence.service.ParticipationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Auto-service du Doctorant connecté sur l'app mobile : son propre profil,
 * les formations disponibles, ses demandes d'inscription et son historique
 * de participation.
 */
@RestController
@RequestMapping("/doctorants/me")
@RequiredArgsConstructor
public class DoctorantMeController {

    private final DoctorantService doctorantService;
    private final FormationService formationService;
    private final ParticipationService participationService;
    private final InscriptionService inscriptionService;
    private final DemandeService demandeService;

    @GetMapping
    public DoctorantDto me(@AuthenticationPrincipal Doctorant doctorant) {
        return doctorantService.getOne(doctorant.getId());
    }

    /**
     * "Les nouvelles formations" — mêmes données que GET /administration/formations,
     * exposées ici pour la cohérence de l'espace Doctorant.
     */
    @GetMapping("/formations")
    public FormationListResponse formations() {
        return new FormationListResponse(formationService.getAll());
    }

    @GetMapping("/participations")
    public List<MesParticipationDto> participations(@AuthenticationPrincipal Doctorant doctorant) {
        return participationService.getMesParticipations(doctorant);
    }

    @GetMapping("/inscriptions")
    public List<InscriptionDto> mesInscriptions(@AuthenticationPrincipal Doctorant doctorant) {
        return inscriptionService.getMine(doctorant);
    }

    /**
     * Le doctorant demande à s'inscrire à une formation (statut EN_ATTENTE,
     * à valider par l'admin depuis le back-office).
     */
    @PostMapping("/inscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public InscriptionDto sinscrire(@AuthenticationPrincipal Doctorant doctorant,
                                     @Valid @RequestBody InscriptionCreateRequest request) {
        return inscriptionService.demanderInscription(doctorant, request.getFormationId());
    }

    /** Le doctorant annule sa propre demande d'inscription tant qu'elle est EN_ATTENTE. */
    @DeleteMapping("/inscriptions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annulerInscription(@AuthenticationPrincipal Doctorant doctorant, @PathVariable Long id) {
        inscriptionService.annulerMaDemande(doctorant, id);
    }

    /**
     * Réclamations de présence soumises par le doctorant ("j'étais présent
     * mais ma présence n'a pas été enregistrée pour ce module").
     */
    @GetMapping("/reclamations")
    public List<DemandeDto> mesReclamations(@AuthenticationPrincipal Doctorant doctorant) {
        return demandeService.getForDoctorant(doctorant.getId()).stream()
                .filter(d -> DemandeService.TYPE_RECLAMATION_PRESENCE.equals(d.getType()))
                .toList();
    }

    @PostMapping("/reclamations")
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeDto signalerPresenceManquante(@AuthenticationPrincipal Doctorant doctorant,
                                                 @Valid @RequestBody ReclamationCreateRequest request) {
        return demandeService.createReclamationPresence(doctorant, request.getModuleId(), request.getDescription());
    }

    /**
     * "2e méthode" de pointage : le doctorant scanne le QR code du module
     * affiché par le formateur en séance. Crée une demande AUTOCHECKIN_PRESENCE
     * en attente — le formateur doit l'approuver depuis "Gestion de présence"
     * pour que la présence compte réellement.
     */
    @PostMapping("/checkin")
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeDto scannerPresence(@AuthenticationPrincipal Doctorant doctorant, @RequestParam Long moduleId) {
        return demandeService.createAutoCheckin(doctorant, moduleId);
    }

    /** Historique des auto-pointages QR du doctorant (en attente, approuvés, rejetés). */
    @GetMapping("/checkins")
    public List<DemandeDto> mesCheckins(@AuthenticationPrincipal Doctorant doctorant) {
        return demandeService.getForDoctorant(doctorant.getId()).stream()
                .filter(d -> DemandeService.TYPE_AUTOCHECKIN_PRESENCE.equals(d.getType()))
                .toList();
    }

    /**
     * Demande de nouvelle carte (carte perdue, endommagée...) — section
     * "Ma carte" de l'app mobile. Traitée par l'admin depuis le back-office
     * comme n'importe quelle Demande.
     */
    @GetMapping("/demandes-carte")
    public List<DemandeDto> mesDemandesCarte(@AuthenticationPrincipal Doctorant doctorant) {
        return demandeService.getForDoctorant(doctorant.getId()).stream()
                .filter(d -> DemandeService.TYPE_NOUVELLE_CARTE.equals(d.getType()))
                .toList();
    }

    @PostMapping("/demandes-carte")
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeDto demanderNouvelleCarte(@AuthenticationPrincipal Doctorant doctorant,
                                             @RequestParam(required = false) String description) {
        return demandeService.createDemandeCarte(doctorant, description);
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal Doctorant doctorant, @Valid @RequestBody ChangePasswordRequest request) {
        doctorantService.changePassword(doctorant, request.getOldPassword(), request.getNewPassword());
    }

    @GetMapping("/settings")
    public UserSettingsDto getSettings(@AuthenticationPrincipal Doctorant doctorant) {
        return doctorantService.getSettings(doctorant);
    }

    @PutMapping("/settings")
    public UserSettingsDto updateSettings(@AuthenticationPrincipal Doctorant doctorant, @RequestBody UserSettingsDto request) {
        return doctorantService.updateSettings(doctorant, request);
    }
}
