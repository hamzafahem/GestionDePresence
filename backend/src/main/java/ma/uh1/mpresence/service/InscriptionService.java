package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.InscriptionDto;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.Formation;
import ma.uh1.mpresence.entity.Inscription;
import ma.uh1.mpresence.entity.StatutWorkflow;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.FormationRepository;
import ma.uh1.mpresence.repository.InscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final DoctorantRepository doctorantRepository;
    private final FormationRepository formationRepository;

    public List<InscriptionDto> getAll() {
        return inscriptionRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<InscriptionDto> getForFormation(Long formationId) {
        return inscriptionRepository.findByFormationId(formationId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<InscriptionDto> getMine(Doctorant doctorant) {
        return inscriptionRepository.findByDoctorantId(doctorant.getId()).stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Auto-service : le doctorant demande à rejoindre une formation (statut EN_ATTENTE).
     */
    public InscriptionDto demanderInscription(Doctorant doctorant, Long formationId) {
        return creerInscription(doctorant.getId(), formationId, StatutWorkflow.EN_ATTENTE, null);
    }

    /**
     * Affectation directe par l'admin : approuvée immédiatement.
     */
    public InscriptionDto affecterDirectement(Long doctorantId, Long formationId, User admin) {
        InscriptionDto dto = creerInscription(doctorantId, formationId, StatutWorkflow.APPROUVEE, admin);
        appliquerFormationSiApprouvee(doctorantId, formationId, StatutWorkflow.APPROUVEE);
        return dto;
    }

    public InscriptionDto traiter(Long id, String statutStr, User traitePar) {
        Inscription inscription = findEntity(id);
        StatutWorkflow statut = StatutWorkflow.valueOf(statutStr);
        inscription.setStatut(statut);
        inscription.setDateTraitement(LocalDateTime.now());
        inscription.setTraitePar(traitePar);
        inscriptionRepository.save(inscription);

        appliquerFormationSiApprouvee(inscription.getDoctorant().getId(), inscription.getFormation().getId(), statut);
        return toDto(inscription);
    }

    public void delete(Long id) {
        if (!inscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscription introuvable: " + id);
        }
        inscriptionRepository.deleteById(id);
    }

    /**
     * Auto-service : le doctorant annule sa propre demande, uniquement si elle
     * est encore EN_ATTENTE (une fois approuvée/rejetée, seul l'admin peut la supprimer).
     */
    public void annulerMaDemande(Doctorant doctorant, Long id) {
        Inscription inscription = findEntity(id);
        if (!inscription.getDoctorant().getId().equals(doctorant.getId())) {
            throw new IllegalArgumentException("Cette demande ne vous appartient pas");
        }
        if (inscription.getStatut() != StatutWorkflow.EN_ATTENTE) {
            throw new IllegalArgumentException("Seule une demande en attente peut être annulée");
        }
        inscriptionRepository.delete(inscription);
    }

    private InscriptionDto creerInscription(Long doctorantId, Long formationId, StatutWorkflow statut, User traitePar) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + doctorantId));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Formation introuvable: " + formationId));

        Inscription inscription = new Inscription();
        inscription.setDoctorant(doctorant);
        inscription.setFormation(formation);
        inscription.setStatut(statut);
        if (statut != StatutWorkflow.EN_ATTENTE) {
            inscription.setDateTraitement(LocalDateTime.now());
            inscription.setTraitePar(traitePar);
        }
        return toDto(inscriptionRepository.save(inscription));
    }

    private void appliquerFormationSiApprouvee(Long doctorantId, Long formationId, StatutWorkflow statut) {
        if (statut != StatutWorkflow.APPROUVEE) {
            return;
        }
        Doctorant doctorant = doctorantRepository.findById(doctorantId).orElseThrow();
        Formation formation = formationRepository.findById(formationId).orElseThrow();
        doctorant.setFormation(formation);
        doctorantRepository.save(doctorant);
    }

    private Inscription findEntity(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscription introuvable: " + id));
    }

    private InscriptionDto toDto(Inscription i) {
        return new InscriptionDto(
                i.getId(),
                i.getDoctorant().getId(), i.getDoctorant().getNom(), i.getDoctorant().getPrenom(),
                i.getFormation().getId(), i.getFormation().getIntitule(),
                i.getStatut().name(), i.getDateCreation(), i.getDateTraitement(),
                i.getTraitePar() != null ? i.getTraitePar().getNomFr() : null
        );
    }
}
