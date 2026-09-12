package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DemandeCreateRequest;
import ma.uh1.mpresence.dto.DemandeDto;
import ma.uh1.mpresence.dto.DemandeTraitementRequest;
import ma.uh1.mpresence.entity.Demande;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.ModuleFormation;
import ma.uh1.mpresence.entity.StatutWorkflow;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DemandeRepository;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.ModuleFormationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemandeService {

    public static final String TYPE_RECLAMATION_PRESENCE = "RECLAMATION_PRESENCE";
    /** Doctorant qui scanne le QR code du module affiché par le formateur en séance. */
    public static final String TYPE_AUTOCHECKIN_PRESENCE = "AUTOCHECKIN_PRESENCE";
    /** Doctorant qui demande une nouvelle carte (carte perdue, endommagée...). */
    public static final String TYPE_NOUVELLE_CARTE = "NOUVELLE_CARTE";

    private final DemandeRepository demandeRepository;
    private final DoctorantRepository doctorantRepository;
    private final ModuleFormationRepository moduleFormationRepository;
    private final ParticipationService participationService;

    public List<DemandeDto> getAll() {
        return demandeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public DemandeDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public List<DemandeDto> getForDoctorant(Long doctorantId) {
        return demandeRepository.findByDoctorantId(doctorantId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public DemandeDto create(DemandeCreateRequest request) {
        Doctorant doctorant = doctorantRepository.findById(request.getDoctorantId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + request.getDoctorantId()));
        return toDto(demandeRepository.save(buildDemande(doctorant, request.getType(), request.getObjet(),
                request.getDescription(), request.getModuleId())));
    }

    /**
     * Auto-service (app mobile) : le doctorant signale une présence
     * manquante pour un module de sa formation. L'admin/formateur la traite
     * ensuite comme n'importe quelle Demande ; son approbation enregistre
     * automatiquement la présence (voir traiter()).
     */
    public DemandeDto createReclamationPresence(Doctorant doctorant, Long moduleId, String description) {
        ModuleFormation module = moduleFormationRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId));
        Demande demande = buildDemande(doctorant, TYPE_RECLAMATION_PRESENCE,
                "Présence manquante — " + module.getNom(), description, moduleId);
        return toDto(demandeRepository.save(demande));
    }

    /**
     * Auto-service (app mobile) : le doctorant scanne le QR code du module
     * affiché par le formateur en séance ("2e méthode" de pointage, en plus
     * du scan de la carte du doctorant par le formateur). Doit être validé
     * par le formateur/admin avant de compter comme présence réelle.
     */
    public DemandeDto createAutoCheckin(Doctorant doctorant, Long moduleId) {
        ModuleFormation module = moduleFormationRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId));
        if (doctorant.getFormation() == null || !doctorant.getFormation().getId().equals(module.getFormation().getId())) {
            throw new IllegalArgumentException("Ce module ne fait pas partie de votre formation");
        }
        Demande demande = buildDemande(doctorant, TYPE_AUTOCHECKIN_PRESENCE,
                "Auto-pointage — " + module.getNom(), "Scan du QR code du module par le doctorant.", moduleId);
        return toDto(demandeRepository.save(demande));
    }

    /**
     * Auto-service (app mobile) : le doctorant demande une nouvelle carte
     * (carte perdue, endommagée...). Traitée comme n'importe quelle Demande
     * par l'admin depuis le back-office (pas de module concerné, pas
     * d'action automatique à l'approbation — juste un changement de statut
     * et une éventuelle réponse, ex. "Carte prête, à récupérer au bureau").
     */
    public DemandeDto createDemandeCarte(Doctorant doctorant, String description) {
        Demande demande = buildDemande(doctorant, TYPE_NOUVELLE_CARTE,
                "Demande de nouvelle carte", description, null);
        return toDto(demandeRepository.save(demande));
    }

    private Demande buildDemande(Doctorant doctorant, String type, String objet, String description, Long moduleId) {
        Demande demande = new Demande();
        demande.setDoctorant(doctorant);
        demande.setType(type);
        demande.setObjet(objet);
        demande.setDescription(description);
        if (moduleId != null) {
            demande.setModule(moduleFormationRepository.findById(moduleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId)));
        }
        return demande;
    }

    public DemandeDto traiter(Long id, DemandeTraitementRequest request, User traitePar) {
        Demande demande = findEntity(id);
        StatutWorkflow nouveauStatut = StatutWorkflow.valueOf(request.getStatut());
        demande.setStatut(nouveauStatut);
        demande.setReponse(request.getReponse());
        demande.setDateTraitement(LocalDateTime.now());
        demande.setTraitePar(traitePar);
        demande = demandeRepository.save(demande);

        boolean estUneDemandeDePresence = TYPE_RECLAMATION_PRESENCE.equals(demande.getType())
                || TYPE_AUTOCHECKIN_PRESENCE.equals(demande.getType());
        if (nouveauStatut == StatutWorkflow.APPROUVEE && estUneDemandeDePresence && demande.getModule() != null) {
            participationService.marquerPresenceManuelle(demande.getModule().getId(), demande.getDoctorant(), traitePar);
        }

        return toDto(demande);
    }

    public void delete(Long id) {
        if (!demandeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Demande introuvable: " + id);
        }
        demandeRepository.deleteById(id);
    }

    private Demande findEntity(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable: " + id));
    }

    private DemandeDto toDto(Demande d) {
        return new DemandeDto(
                d.getId(),
                d.getDoctorant().getId(), d.getDoctorant().getNom(), d.getDoctorant().getPrenom(),
                d.getType(), d.getObjet(), d.getDescription(), d.getStatut().name(),
                d.getDateCreation(), d.getDateTraitement(),
                d.getTraitePar() != null ? d.getTraitePar().getNomFr() : null,
                d.getReponse(),
                d.getModule() != null ? d.getModule().getId() : null,
                d.getModule() != null ? d.getModule().getNom() : null
        );
    }
}
