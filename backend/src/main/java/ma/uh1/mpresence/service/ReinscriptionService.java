package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ReinscriptionCreateRequest;
import ma.uh1.mpresence.dto.ReinscriptionDto;
import ma.uh1.mpresence.dto.ReinscriptionTraitementRequest;
import ma.uh1.mpresence.entity.AnneeUniversitaire;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.Reinscription;
import ma.uh1.mpresence.entity.StatutWorkflow;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.AnneeUniversitaireRepository;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.ReinscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReinscriptionService {

    private final ReinscriptionRepository reinscriptionRepository;
    private final DoctorantRepository doctorantRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;

    public List<ReinscriptionDto> getAll() {
        return reinscriptionRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public ReinscriptionDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public ReinscriptionDto create(ReinscriptionCreateRequest request) {
        Doctorant doctorant = doctorantRepository.findById(request.getDoctorantId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + request.getDoctorantId()));
        AnneeUniversitaire annee = anneeUniversitaireRepository.findById(request.getAnneeUniversitaireId())
                .orElseThrow(() -> new ResourceNotFoundException("Année universitaire introuvable: " + request.getAnneeUniversitaireId()));

        Reinscription reinscription = new Reinscription();
        reinscription.setDoctorant(doctorant);
        reinscription.setAnneeUniversitaire(annee);
        return toDto(reinscriptionRepository.save(reinscription));
    }

    public ReinscriptionDto traiter(Long id, ReinscriptionTraitementRequest request, User traitePar) {
        Reinscription reinscription = findEntity(id);
        reinscription.setStatut(StatutWorkflow.valueOf(request.getStatut()));
        reinscription.setCommentaire(request.getCommentaire());
        reinscription.setDateTraitement(LocalDateTime.now());
        reinscription.setTraitePar(traitePar);
        return toDto(reinscriptionRepository.save(reinscription));
    }

    public void delete(Long id) {
        if (!reinscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Réinscription introuvable: " + id);
        }
        reinscriptionRepository.deleteById(id);
    }

    private Reinscription findEntity(Long id) {
        return reinscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réinscription introuvable: " + id));
    }

    private ReinscriptionDto toDto(Reinscription r) {
        return new ReinscriptionDto(
                r.getId(),
                r.getDoctorant().getId(), r.getDoctorant().getNom(), r.getDoctorant().getPrenom(),
                r.getAnneeUniversitaire().getId(), r.getAnneeUniversitaire().getLibelle(),
                r.getStatut().name(), r.getDateCreation(), r.getDateTraitement(),
                r.getTraitePar() != null ? r.getTraitePar().getNomFr() : null,
                r.getCommentaire()
        );
    }
}
