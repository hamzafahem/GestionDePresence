package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DemandeAffectationFormateurDto;
import ma.uh1.mpresence.entity.DemandeAffectationFormateur;
import ma.uh1.mpresence.entity.Formation;
import ma.uh1.mpresence.entity.ModuleFormation;
import ma.uh1.mpresence.entity.StatutWorkflow;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DemandeAffectationFormateurRepository;
import ma.uh1.mpresence.repository.FormationRepository;
import ma.uh1.mpresence.repository.ModuleFormationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Auto-service du Formateur : demande d'affectation à une Formation entière
 * ou à un Module précis, approuvée/rejetée par l'admin depuis le
 * back-office. Vient en complément de l'affectation directe par l'admin
 * (FormationService/ModuleService.affecterFormateur), qui reste disponible
 * en parallèle.
 */
@Service
@RequiredArgsConstructor
public class AffectationFormateurService {

    private final DemandeAffectationFormateurRepository demandeRepository;
    private final FormationRepository formationRepository;
    private final ModuleFormationRepository moduleFormationRepository;
    private final FormationService formationService;
    private final ModuleService moduleService;

    public List<DemandeAffectationFormateurDto> getMine(Long formateurId) {
        return demandeRepository.findByFormateurIdOrderByDateCreationDesc(formateurId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public List<DemandeAffectationFormateurDto> getAll() {
        return demandeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public DemandeAffectationFormateurDto demanderFormation(User formateur, Long formationId) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Formation introuvable: " + formationId));
        ensurePasDeDoublon(formateur.getId(), formationId, null);

        DemandeAffectationFormateur demande = new DemandeAffectationFormateur();
        demande.setFormateur(formateur);
        demande.setFormation(formation);
        return toDto(demandeRepository.save(demande));
    }

    public DemandeAffectationFormateurDto demanderModule(User formateur, Long moduleId) {
        ModuleFormation module = moduleFormationRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId));
        ensurePasDeDoublon(formateur.getId(), module.getFormation().getId(), moduleId);

        DemandeAffectationFormateur demande = new DemandeAffectationFormateur();
        demande.setFormateur(formateur);
        demande.setFormation(module.getFormation());
        demande.setModule(module);
        return toDto(demandeRepository.save(demande));
    }

    public void annuler(User formateur, Long demandeId) {
        DemandeAffectationFormateur demande = findEntity(demandeId);
        if (!demande.getFormateur().getId().equals(formateur.getId())) {
            throw new IllegalArgumentException("Cette demande ne vous appartient pas");
        }
        if (demande.getStatut() != StatutWorkflow.EN_ATTENTE) {
            throw new IllegalArgumentException("Seule une demande en attente peut être annulée");
        }
        demandeRepository.delete(demande);
    }

    public DemandeAffectationFormateurDto traiter(Long demandeId, String statutStr, String reponse, User traitePar) {
        DemandeAffectationFormateur demande = findEntity(demandeId);
        StatutWorkflow statut = StatutWorkflow.valueOf(statutStr);
        demande.setStatut(statut);
        demande.setDateTraitement(LocalDateTime.now());
        demande.setTraitePar(traitePar);
        demande.setReponse(reponse);
        demandeRepository.save(demande);

        if (statut == StatutWorkflow.APPROUVEE) {
            if (demande.getModule() != null) {
                moduleService.affecterFormateur(demande.getFormation().getId(), demande.getModule().getId(), demande.getFormateur().getId());
            } else {
                formationService.affecterFormateur(demande.getFormation().getId(), demande.getFormateur().getId());
            }
        }
        return toDto(demande);
    }

    public void delete(Long id) {
        if (!demandeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Demande introuvable: " + id);
        }
        demandeRepository.deleteById(id);
    }

    private void ensurePasDeDoublon(Long formateurId, Long formationId, Long moduleId) {
        boolean existeDeja = demandeRepository.findByFormateurIdOrderByDateCreationDesc(formateurId).stream()
                .anyMatch(d -> d.getStatut() == StatutWorkflow.EN_ATTENTE
                        && d.getFormation().getId().equals(formationId)
                        && ((moduleId == null && d.getModule() == null)
                            || (moduleId != null && d.getModule() != null && d.getModule().getId().equals(moduleId))));
        if (existeDeja) {
            throw new IllegalArgumentException("Une demande est déjà en attente pour cette affectation");
        }
    }

    private DemandeAffectationFormateur findEntity(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable: " + id));
    }

    private DemandeAffectationFormateurDto toDto(DemandeAffectationFormateur d) {
        return new DemandeAffectationFormateurDto(
                d.getId(),
                d.getFormateur().getId(), d.getFormateur().getNomFr(), d.getFormateur().getPrenomFr(),
                d.getFormation().getId(), d.getFormation().getIntitule(),
                d.getModule() != null ? d.getModule().getId() : null,
                d.getModule() != null ? d.getModule().getNom() : null,
                d.getStatut().name(), d.getDateCreation(), d.getDateTraitement(),
                d.getTraitePar() != null ? d.getTraitePar().getNomFr() : null,
                d.getReponse()
        );
    }
}
