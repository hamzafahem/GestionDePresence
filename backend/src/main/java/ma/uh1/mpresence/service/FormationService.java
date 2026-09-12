package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.FormationDto;
import ma.uh1.mpresence.dto.FormationUpsertRequest;
import ma.uh1.mpresence.entity.Formation;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.AnneeUniversitaireRepository;
import ma.uh1.mpresence.repository.FormationRepository;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormationService {

    private final FormationRepository formationRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;
    private final UserRepository userRepository;

    public List<FormationDto> getAll() {
        return formationRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public FormationDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public FormationDto create(FormationUpsertRequest request) {
        Formation formation = new Formation();
        applyRequest(formation, request);
        return toDto(formationRepository.save(formation));
    }

    public FormationDto update(Long id, FormationUpsertRequest request) {
        Formation formation = findEntity(id);
        applyRequest(formation, request);
        return toDto(formationRepository.save(formation));
    }

    public void delete(Long id) {
        if (!formationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Formation introuvable: " + id);
        }
        formationRepository.deleteById(id);
    }

    private Formation findEntity(Long id) {
        return formationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formation introuvable: " + id));
    }

    public FormationDto affecterFormateur(Long id, Long formateurId) {
        Formation formation = findEntity(id);
        if (formateurId == null) {
            formation.setFormateur(null);
        } else {
            User formateur = userRepository.findById(formateurId)
                    .orElseThrow(() -> new ResourceNotFoundException("Formateur introuvable: " + formateurId));
            formation.setFormateur(formateur);
        }
        return toDto(formationRepository.save(formation));
    }

    private void applyRequest(Formation formation, FormationUpsertRequest request) {
        formation.setIntitule(request.getIntitule());
        formation.setDescription(request.getDescription());
        formation.setDateDebut(request.getDateDebut());
        formation.setDateFin(request.getDateFin());

        if (formation.getAnneeUniversitaire() == null) {
            anneeUniversitaireRepository.findByActiveTrue().ifPresent(formation::setAnneeUniversitaire);
        }

        if (request.getFormateurId() != null) {
            User formateur = userRepository.findById(request.getFormateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Formateur introuvable: " + request.getFormateurId()));
            formation.setFormateur(formateur);
        }
    }

    private FormationDto toDto(Formation formation) {
        return new FormationDto(
                formation.getId(),
                formation.getIntitule(),
                formation.getDescription(),
                formation.getDateDebut(),
                formation.getDateFin(),
                formation.getFormateur() != null ? formation.getFormateur().getId() : null,
                formation.getFormateur() != null ? formation.getFormateur().getPrenomFr() + " " + formation.getFormateur().getNomFr() : null
        );
    }
}
