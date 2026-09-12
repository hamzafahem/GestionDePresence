package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.MesParticipationDto;
import ma.uh1.mpresence.dto.ParticipationResultDto;
import ma.uh1.mpresence.dto.PresenceDto;
import ma.uh1.mpresence.dto.PresenceRosterRowDto;
import ma.uh1.mpresence.entity.Demande;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.ModuleFormation;
import ma.uh1.mpresence.entity.Participation;
import ma.uh1.mpresence.entity.StatutWorkflow;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DemandeRepository;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.ModuleFormationRepository;
import ma.uh1.mpresence.repository.ParticipationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ModuleFormationRepository moduleFormationRepository;
    private final DoctorantRepository doctorantRepository;
    private final ParticipationRepository participationRepository;
    private final DemandeRepository demandeRepository;

    /**
     * Correspond au scan/saisie du "Code Apogée" dans hp/qrcode côté front :
     * le paramètre "qr" scanné identifie le DOCTORANT (pas le module).
     */
    public ParticipationResultDto participerParQr(Long formationId, Long moduleId, String codeApogeeScanned, User recordedBy) {
        ModuleFormation module = moduleFormationRepository.findByIdAndFormationId(moduleId, formationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module " + moduleId + " introuvable pour la formation " + formationId));

        Doctorant doctorant = doctorantRepository.findByCodeApogeeIgnoreCase(codeApogeeScanned).orElse(null);
        if (doctorant == null) {
            return new ParticipationResultDto(false, "Aucun doctorant trouvé pour ce code", null, null, null, false);
        }

        boolean cree = marquerPresence(module, doctorant, recordedBy, codeApogeeScanned);
        String message = cree ? "Présence enregistrée avec succès" : "Présence déjà enregistrée aujourd'hui";
        return new ParticipationResultDto(true, message, doctorant.getNom(), doctorant.getPrenom(), doctorant.getId(), !cree);
    }

    /**
     * Marquage manuel depuis la vue "Gestion de présence" (Formateur) ou
     * suite à l'approbation d'une réclamation de présence.
     */
    public void marquerPresenceManuelle(Long moduleId, Doctorant doctorant, User recordedBy) {
        ModuleFormation module = moduleFormationRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId));
        marquerPresence(module, doctorant, recordedBy, null);
    }

    /**
     * Retire la présence du jour d'un doctorant pour un module (annule un
     * marquage fait par erreur).
     */
    public void retirerPresence(Long moduleId, Long doctorantId) {
        LocalDate today = LocalDate.now();
        participationRepository.findFirstByDoctorantIdAndModuleIdAndDateParticipationBetween(
                        doctorantId, moduleId, today.atStartOfDay(), today.atTime(LocalTime.MAX))
                .ifPresent(participationRepository::delete);
    }

    /**
     * Vue "Gestion de présence" (Formateur) : tous les doctorants de la
     * formation du module, avec leur statut de présence du jour et une
     * éventuelle réclamation en attente pour ce module.
     */
    public List<PresenceRosterRowDto> getRoster(Long formationId, Long moduleId) {
        ModuleFormation module = moduleFormationRepository.findByIdAndFormationId(moduleId, formationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module " + moduleId + " introuvable pour la formation " + formationId));

        LocalDate today = LocalDate.now();
        Map<Long, Participation> presencesAujourdhui = participationRepository.findByModuleId(module.getId()).stream()
                .filter(p -> p.getDateParticipation().toLocalDate().isEqual(today))
                .collect(Collectors.toMap(p -> p.getDoctorant().getId(), p -> p, (a, b) -> a));

        Map<Long, Demande> reclamationsEnAttente = demandeRepository
                .findByModuleIdAndStatut(module.getId(), StatutWorkflow.EN_ATTENTE).stream()
                .collect(Collectors.toMap(d -> d.getDoctorant().getId(), d -> d, (a, b) -> a));

        return doctorantRepository.findByFormationId(formationId).stream()
                .map(doctorant -> {
                    Participation presence = presencesAujourdhui.get(doctorant.getId());
                    Demande reclamation = reclamationsEnAttente.get(doctorant.getId());
                    return new PresenceRosterRowDto(
                            doctorant.getId(), doctorant.getNom(), doctorant.getPrenom(), doctorant.getCodeApogee(),
                            doctorant.getPhoto(),
                            presence != null,
                            presence != null ? presence.getDateParticipation() : null,
                            reclamation != null ? reclamation.getId() : null,
                            reclamation != null ? reclamation.getType() : null
                    );
                })
                .collect(Collectors.toList());
    }

    /** @return true si une nouvelle présence a été créée, false si déjà enregistrée aujourd'hui. */
    private boolean marquerPresence(ModuleFormation module, Doctorant doctorant, User recordedBy, String codeScanned) {
        LocalDate today = LocalDate.now();
        Optional<Participation> existante = participationRepository
                .findFirstByDoctorantIdAndModuleIdAndDateParticipationBetween(
                        doctorant.getId(), module.getId(), today.atStartOfDay(), today.atTime(LocalTime.MAX));

        if (existante.isPresent()) {
            return false;
        }

        Participation participation = new Participation();
        participation.setDoctorant(doctorant);
        participation.setModule(module);
        participation.setRecordedBy(recordedBy);
        participation.setCodeScanned(codeScanned);
        participationRepository.save(participation);
        return true;
    }

    /**
     * Feuille de présence d'un module (pour hp/create-pdf côté front).
     */
    public List<PresenceDto> getPresences(Long formationId, Long moduleId) {
        moduleFormationRepository.findByIdAndFormationId(moduleId, formationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Module " + moduleId + " introuvable pour la formation " + formationId));

        return participationRepository.findByModuleId(moduleId).stream()
                .map(p -> new PresenceDto(
                        p.getId(),
                        p.getDoctorant().getNom(),
                        p.getDoctorant().getPrenom(),
                        p.getDoctorant().getCne(),
                        p.getDoctorant().getCodeApogee(),
                        p.getDateParticipation(),
                        p.getRecordedBy() != null ? p.getRecordedBy().getNomFr() : null
                ))
                .collect(Collectors.toList());
    }

    /**
     * Historique de présence du doctorant connecté (Accueil de l'app mobile).
     */
    public List<MesParticipationDto> getMesParticipations(Doctorant doctorant) {
        return participationRepository.findByDoctorantIdOrderByDateParticipationDesc(doctorant.getId()).stream()
                .map(p -> new MesParticipationDto(
                        p.getModule().getFormation().getId(),
                        p.getModule().getFormation().getIntitule(),
                        p.getModule().getId(),
                        p.getModule().getNom(),
                        p.getDateParticipation()
                ))
                .collect(Collectors.toList());
    }
}
