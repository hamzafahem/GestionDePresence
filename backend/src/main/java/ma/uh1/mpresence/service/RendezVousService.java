package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.RendezVousDto;
import ma.uh1.mpresence.dto.RendezVousUpsertRequest;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.RendezVous;
import ma.uh1.mpresence.entity.StatutRendezVous;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.RendezVousRepository;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final DoctorantRepository doctorantRepository;
    private final UserRepository userRepository;

    public List<RendezVousDto> getAll() {
        return rendezVousRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public RendezVousDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public RendezVousDto create(RendezVousUpsertRequest request) {
        RendezVous rdv = new RendezVous();
        applyRequest(rdv, request);
        return toDto(rendezVousRepository.save(rdv));
    }

    public RendezVousDto update(Long id, RendezVousUpsertRequest request) {
        RendezVous rdv = findEntity(id);
        applyRequest(rdv, request);
        return toDto(rendezVousRepository.save(rdv));
    }

    public void delete(Long id) {
        if (!rendezVousRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rendez-vous introuvable: " + id);
        }
        rendezVousRepository.deleteById(id);
    }

    private void applyRequest(RendezVous rdv, RendezVousUpsertRequest request) {
        Doctorant doctorant = doctorantRepository.findById(request.getDoctorantId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + request.getDoctorantId()));
        rdv.setDoctorant(doctorant);

        if (request.getFormateurId() != null) {
            User formateur = userRepository.findById(request.getFormateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Formateur introuvable: " + request.getFormateurId()));
            rdv.setFormateur(formateur);
        } else {
            rdv.setFormateur(null);
        }

        rdv.setObjet(request.getObjet());
        rdv.setDateHeure(request.getDateHeure());
        rdv.setLieu(request.getLieu());
        rdv.setCommentaire(request.getCommentaire());
        if (request.getStatut() != null) {
            rdv.setStatut(StatutRendezVous.valueOf(request.getStatut()));
        }
    }

    private RendezVous findEntity(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous introuvable: " + id));
    }

    private RendezVousDto toDto(RendezVous r) {
        return new RendezVousDto(
                r.getId(),
                r.getDoctorant().getId(), r.getDoctorant().getNom(), r.getDoctorant().getPrenom(),
                r.getFormateur() != null ? r.getFormateur().getId() : null,
                r.getFormateur() != null ? r.getFormateur().getNomFr() : null,
                r.getObjet(), r.getDateHeure(), r.getLieu(), r.getStatut().name(), r.getCommentaire()
        );
    }
}
