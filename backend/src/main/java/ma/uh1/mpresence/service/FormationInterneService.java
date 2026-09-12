package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DoctorantDto;
import ma.uh1.mpresence.dto.FormationInterneDto;
import ma.uh1.mpresence.dto.FormationInterneUpsertRequest;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.FormationInterne;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.FormationInterneRepository;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormationInterneService {

    private final FormationInterneRepository formationInterneRepository;
    private final UserRepository userRepository;
    private final DoctorantRepository doctorantRepository;

    public List<FormationInterneDto> getAll() {
        return formationInterneRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public FormationInterneDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public FormationInterneDto create(FormationInterneUpsertRequest request) {
        FormationInterne formation = new FormationInterne();
        applyRequest(formation, request);
        return toDto(formationInterneRepository.save(formation));
    }

    public FormationInterneDto update(Long id, FormationInterneUpsertRequest request) {
        FormationInterne formation = findEntity(id);
        applyRequest(formation, request);
        return toDto(formationInterneRepository.save(formation));
    }

    public void delete(Long id) {
        if (!formationInterneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Formation interne introuvable: " + id);
        }
        formationInterneRepository.deleteById(id);
    }

    public FormationInterneDto inscrire(Long id, Long doctorantId) {
        FormationInterne formation = findEntity(id);
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + doctorantId));
        formation.getParticipants().add(doctorant);
        return toDto(formationInterneRepository.save(formation));
    }

    public FormationInterneDto desinscrire(Long id, Long doctorantId) {
        FormationInterne formation = findEntity(id);
        formation.getParticipants().removeIf(d -> d.getId().equals(doctorantId));
        return toDto(formationInterneRepository.save(formation));
    }

    public List<DoctorantDto> getParticipants(Long id) {
        return findEntity(id).getParticipants().stream()
                .map(d -> new DoctorantDto(
                        d.getId(), d.getNom(), d.getPrenom(), d.getEmail(), d.getMobile(),
                        d.getCin(), d.getCne(), d.getCodeApogee(), d.getFiliere(), d.getNationalite(), d.getPhoto(),
                        d.getDateNaissance(), d.getDetails(),
                        d.getFormation() != null ? d.getFormation().getId() : null,
                        d.getFormation() != null ? d.getFormation().getIntitule() : null,
                        d.getFormation() != null && d.getFormation().getAnneeUniversitaire() != null
                                ? d.getFormation().getAnneeUniversitaire().getLibelle() : null,
                        d.getStatut().name(),
                        d.getEncadrant() != null ? d.getEncadrant().getId() : null,
                        d.getEncadrant() != null ? d.getEncadrant().getPrenomFr() + " " + d.getEncadrant().getNomFr() : null,
                        d.getDateCreation()
                ))
                .collect(Collectors.toList());
    }

    private void applyRequest(FormationInterne formation, FormationInterneUpsertRequest request) {
        formation.setIntitule(request.getIntitule());
        formation.setDescription(request.getDescription());
        formation.setDateDebut(request.getDateDebut());
        formation.setDateFin(request.getDateFin());
        formation.setLieu(request.getLieu());
        formation.setCapaciteMax(request.getCapaciteMax());

        if (request.getFormateurId() != null) {
            User formateur = userRepository.findById(request.getFormateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Formateur introuvable: " + request.getFormateurId()));
            formation.setFormateur(formateur);
        } else {
            formation.setFormateur(null);
        }
    }

    private FormationInterne findEntity(Long id) {
        return formationInterneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formation interne introuvable: " + id));
    }

    private FormationInterneDto toDto(FormationInterne f) {
        return new FormationInterneDto(
                f.getId(), f.getIntitule(), f.getDescription(),
                f.getFormateur() != null ? f.getFormateur().getId() : null,
                f.getFormateur() != null ? f.getFormateur().getNomFr() : null,
                f.getDateDebut(), f.getDateFin(), f.getLieu(), f.getCapaciteMax(),
                f.getParticipants().size()
        );
    }
}
